<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Добавить лицо</title>
</head>
<body>
<h2>Ввод данных лица</h2>
<form action="/persons/save" method="post">

    <label>Имя:</label>
    <input type="text" name="name" required/>
    <br/><br/>

    <label>Выберите телефоны:</label><br/>
    <#list allPhones as phone>
        <input type="checkbox" name="phoneIds" value="${phone.id}"/>
        ${phone.number}
        <br/>
    </#list>
    <br/>

    <label>Или добавьте новый:</label>
    <input type="text" name="newPhone" placeholder="+7..."/>
    <br/><br/>

    <button type="submit">Сохранить</button>
</form>
<br/>
<a href="/persons/all">К списку</a>
</body>
</html>