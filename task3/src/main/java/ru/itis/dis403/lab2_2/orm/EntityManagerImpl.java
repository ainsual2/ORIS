package ru.itis.dis403.lab2_2.orm;

import ru.itis.dis403.lab2_2.orm.annotations.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntityManagerImpl implements EntityManager, AutoCloseable {

    private Connection connection;

    public EntityManagerImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public <T> T save(T entity) {
        Class<?> clazz = entity.getClass();
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);

        if (entityAnnotation == null) {
            throw new RuntimeException("Class " + clazz.getName() + " is not an entity");
        }

        String tableName = getTableName(clazz);
        Field idField = getIdField(clazz);
        idField.setAccessible(true);  // <-- ВАЖНО:设置 accessible сразу

        try {
            Object idValue = idField.get(entity);

            if (idValue == null || (idValue instanceof Number && ((Number) idValue).longValue() == 0)) {
                return insert(entity, clazz, tableName, idField);
            } else {
                return update(entity, clazz, tableName, idField);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing fields", e);
        }
    }

    private <T> T insert(T entity, Class<?> clazz, String tableName, Field idField)
            throws IllegalAccessException {
        List<Field> columns = getColumnFields(clazz);

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append(" (");

        StringBuilder values = new StringBuilder();
        List<Object> params = new ArrayList<>();
        int paramIndex = 0;

        for (Field field : columns) {
            if (field.isAnnotationPresent(Id.class)) {
                Id idAnnotation = field.getAnnotation(Id.class);
                if (idAnnotation.generated()) {
                    continue;
                }
            }

            field.setAccessible(true);

            if (paramIndex > 0) {
                sql.append(", ");
                values.append(", ");
            }

            String columnName = getColumnName(field);
            sql.append(columnName);
            values.append("?");

            Object rawValue = field.get(entity);

            // Если поле ManyToOne — положить не объект, а его id
            Object paramValue = rawValue;
            ManyToOne mto = field.getAnnotation(ManyToOne.class);
            if (mto != null && rawValue != null) {
                // пытаемся найти @Id поле у связанной сущности и взять его значение
                Field refIdField = getIdField(rawValue.getClass());
                refIdField.setAccessible(true);
                paramValue = refIdField.get(rawValue);
            }

            params.add(paramValue);
            paramIndex++;
        }

        sql.append(") VALUES (").append(values).append(") RETURNING id");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                setParameter(ps, i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long generatedId = rs.getLong(1);
                idField.set(entity, generatedId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing INSERT", e);
        }

        return entity;
    }

    private <T> T update(T entity, Class<?> clazz, String tableName, Field idField)
            throws IllegalAccessException {
        List<Field> columns = getColumnFields(clazz);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName).append(" SET ");

        List<Object> params = new ArrayList<>();

        // ---- В методе update(...) при сборе params ----
        for (Field field : columns) {
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }

            field.setAccessible(true);

            if (!params.isEmpty()) {
                sql.append(", ");
            }

            String columnName = getColumnName(field);
            sql.append(columnName).append(" = ?");

            Object rawValue = field.get(entity);
            ManyToOne mto = field.getAnnotation(ManyToOne.class);
            if (mto != null && rawValue != null) {
                Field refIdField = getIdField(rawValue.getClass());
                refIdField.setAccessible(true);
                Object refIdValue = refIdField.get(rawValue);
                params.add(refIdValue);
            } else {
                params.add(rawValue);
            }
        }

        Object idValue = idField.get(entity);
        sql.append(" WHERE id = ?");
        params.add(idValue);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                setParameter(ps, i + 1, params.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing UPDATE", e);
        }

        return entity;
    }

    @Override
    public void remove(Object entity) {
        Class<?> clazz = entity.getClass();
        String tableName = getTableName(clazz);
        Field idField = getIdField(clazz);

        try {
            idField.setAccessible(true);  // <-- ВАЖНО
            Object idValue = idField.get(entity);

            String sql = "DELETE FROM " + tableName + " WHERE id = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setObject(1, idValue);
                ps.executeUpdate();
            }
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException("Error executing DELETE", e);
        }
    }

    @Override
    public <T> T find(Class<T> entityType, Object key) {
        Entity entityAnnotation = entityType.getAnnotation(Entity.class);
        if (entityAnnotation == null) {
            throw new RuntimeException("Class " + entityType.getName() + " is not an entity");
        }

        String tableName = getTableName(entityType);
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, key);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToEntity(rs, entityType);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing FIND", e);
        }

        return null;
    }

    @Override
    public <T> List<T> findAll(Class<T> entityType) {
        Entity entityAnnotation = entityType.getAnnotation(Entity.class);
        if (entityAnnotation == null) {
            throw new RuntimeException("Class " + entityType.getName() + " is not an entity");
        }

        String tableName = getTableName(entityType);
        String sql = "SELECT * FROM " + tableName;

        List<T> results = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                T entity = mapResultSetToEntity(rs, entityType);
                results.add(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing FIND_ALL", e);
        }

        return results;
    }

    // Вспомогательные методы

    private String getTableName(Class<?> clazz) {
        Entity annotation = clazz.getAnnotation(Entity.class);
        if (annotation != null && !annotation.value().isEmpty()) {
            return annotation.value();
        }
        return clazz.getSimpleName().toLowerCase();
    }

    private Field getIdField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new RuntimeException("No @Id field found in " + clazz.getName());
    }

    private List<Field> getColumnFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) ||
                    field.isAnnotationPresent(Id.class) ||
                    field.isAnnotationPresent(ManyToOne.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    private String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null && !column.value().isEmpty()) {
            return column.value();
        }
        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        if (manyToOne != null) {
            return field.getName() + "_id";
        }
        return field.getName();
    }

    private <T> T mapResultSetToEntity(ResultSet rs, Class<T> entityType) throws SQLException {
        try {
            T entity = entityType.getDeclaredConstructor().newInstance();

            // ---- В методе mapResultSetToEntity(...) заменяем поведение при ManyToOne ----
            for (Field field : entityType.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class) ||
                        field.isAnnotationPresent(Id.class) ||
                        field.isAnnotationPresent(ManyToOne.class)) {

                    field.setAccessible(true);

                    String columnName = getColumnName(field);

                    // если это ManyToOne — в БД хранится fk (id), а в объекте должно быть вложенное значение
                    if (field.isAnnotationPresent(ManyToOne.class)) {
                        Class<?> refType = field.getType();
                        Object fkValue = getObjectFromResultSet(rs, columnName, Long.class); // обычно long/id
                        if (fkValue != null) {
                            try {
                                Object refInstance = refType.getDeclaredConstructor().newInstance();
                                Field refId = getIdField(refType);
                                refId.setAccessible(true);
                                // возможно нужно конвертировать тип (Integer/Long)
                                refId.set(refInstance, convertToFieldType(refId.getType(), fkValue));
                                field.set(entity, refInstance);
                            } catch (Exception ex) {
                                throw new RuntimeException("Error creating referenced entity instance", ex);
                            }
                        } else {
                            field.set(entity, null);
                        }
                    } else {
                        Object value = getObjectFromResultSet(rs, columnName, field.getType());
                        field.set(entity, value);
                    }
                }
            }

            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping ResultSet to entity", e);
        }
    }

    private Object getObjectFromResultSet(ResultSet rs, String columnName, Class<?> type)
            throws SQLException {
        Object value = rs.getObject(columnName);

        if (value == null) {
            return null;
        }

        if (type.isEnum()) {
            return Enum.valueOf((Class<Enum>) type, value.toString());
        }

        return value;
    }
    private Object convertToFieldType(Class<?> fieldType, Object value) {
        if (value == null) return null;
        if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
            if (value instanceof Number) return ((Number) value).longValue();
            return Long.valueOf(value.toString());
        }
        if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
            if (value instanceof Number) return ((Number) value).intValue();
            return Integer.valueOf(value.toString());
        }
        if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
            if (value instanceof Number) return ((Number) value).shortValue();
            return Short.valueOf(value.toString());
        }
        // fallback
        return value;
    }

    private void setParameter(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.NULL);
        } else if (value instanceof BigDecimal) {
            ps.setBigDecimal(index, (BigDecimal) value);
        } else if (value instanceof Integer) {
            ps.setInt(index, (Integer) value);
        } else if (value instanceof Long) {
            ps.setLong(index, (Long) value);
        } else if (value instanceof String) {
            ps.setString(index, (String) value);
        } else if (value instanceof Enum) {
            ps.setString(index, ((Enum<?>) value).name());
        } else {
            ps.setObject(index, value);
        }
    }
}