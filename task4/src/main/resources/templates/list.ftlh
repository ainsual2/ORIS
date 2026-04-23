<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Все лица</title>
</head>
<body>
<h2>Список всех лиц</h2>

<#if persons?has_content>
    <table border="1" cellpadding="5">
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Телефоны</th>
        </tr>
        <#list persons as p>
            <tr>
                <td>${p.id!}</td>
                <td>${p.name!}</td>
                <td>
                    <#if p.phones?has_content>
                        <#list p.phones as ph>
                            ${ph?string} <br/>
                        </#list>
                    <#else>
                        -
                    </#if>
                </td>
            </tr>
        </#list>
    </table>
<#else>
    <p>Список пуст.</p>
</#if>

<br/>
<a href="/persons/new">Добавить новое лицо</a>
</body>
</html>