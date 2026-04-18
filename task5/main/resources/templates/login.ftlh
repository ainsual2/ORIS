<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Тест</title>
</head>
<body>

<form id="form" method="post" action="/app">
    <div><input name="username" type="text" placeholder="username"/></div>
    <div><input name="password" type="password" placeholder="password"/></div>
    <div><input type="button" value="OK" onclick="login()"/></div>
</form>

<div id="content_section" style="display:none">
    <h3>Данные по бронированию в гостинице:</h3>
    <div>
        <table id="booking_table"></table>
    </div>
</div>

<div id="booking_data" style="display:none; margin-top:20px;">
    <h3>Редактирование бронирования</h3>
    <input type="hidden" id="edit_id"/>
    <div><label>Комната: <input type="text" id="edit_room"/></label></div>
    <div><label>Дата заезда: <input type="date" id="edit_arrivaldate"/></label></div>
    <div><label>Дата проживания: <input type="date" id="edit_stayingdate"/></label></div>
    <div><label>Дата выезда: <input type="date" id="edit_departuredate"/></label></div>
    <div><label>Имя гостя: <input type="text" id="edit_name" readonly/></label></div>
    <div><label>Пол: <input type="text" id="edit_gender" readonly/></label></div>
    <div><button onclick="saveBooking()">Сохранить изменения</button></div>
</div>

<script>
    let jwt_token;

    function login() {
        const formDataToJson = (formData) => JSON.stringify(Object.fromEntries(formData));
        const formElement = document.getElementById('form');
        const jsonData = formDataToJson(new FormData(formElement));
        console.log(jsonData);

        fetch('http://localhost:8090/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json;charset=utf-8' },
            body: jsonData
        })
            .then(response => response.json())
            .then(json => {
                jwt_token = json.token;
                document.getElementById("form").style.display = "none";
                document.getElementById("content_section").style.display = "block";
                show_content();
            });
    }

    function show_content() {
        show_bookings();
    }

    function show_bookings() {
        fetch('http://localhost:8090/api/booking/allview', {
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + jwt_token }
        })
            .then(response => response.json())
            .then(json => { make_booking_table(json); });
    }

    function make_booking_table(data) {
        let tbl = document.getElementById("booking_table");
        tbl.innerHTML = "";
        for (let i = 0; i < data.bookings.length; i++) {
            let tr = document.createElement('tr');
            let td_room = document.createElement('td');
            td_room.innerHTML = "<span style='cursor:pointer;color:blue;text-decoration:underline' onclick='open_booking("
                + data.bookings[i].id + ")'>" + data.bookings[i].room + "</span>";
            tr.append(td_room);
            let td_arrivaldate = document.createElement('td');
            td_arrivaldate.innerHTML = data.bookings[i].arrivaldate;
            tr.append(td_arrivaldate);
            let td_stayingdate = document.createElement('td');
            td_stayingdate.innerHTML = data.bookings[i].stayingdate;
            tr.append(td_stayingdate);
            tbl.append(tr);
        }
    }

    function open_booking(booking_id) {
        fetch('http://localhost:8090/api/booking/get/' + booking_id, {
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + jwt_token }
        })
            .then(response => response.json())
            .then(json => { fill_booking(json); });
    }

    function fill_booking(data) {
        document.getElementById("edit_id").value = data.id;
        document.getElementById("edit_room").value = data.room || '';
        document.getElementById("edit_arrivaldate").value = formatDate(data.arrivaldate);
        document.getElementById("edit_stayingdate").value = formatDate(data.stayingdate);
        document.getElementById("edit_departuredate").value = formatDate(data.departuredate);
        document.getElementById("edit_name").value = data.name || '';
        document.getElementById("edit_gender").value = data.gender || '';
        document.getElementById("booking_data").style.display = "block";
    }

    function formatDate(dateVal) {
        if (!dateVal) return '';
        return new Date(dateVal).toISOString().split('T')[0];
    }

    function saveBooking() {
        const id = document.getElementById("edit_id").value;
        const body = {
            id: parseInt(id),
            room: document.getElementById("edit_room").value,
            arrivaldate: document.getElementById("edit_arrivaldate").value,
            stayingdate: document.getElementById("edit_stayingdate").value,
            departuredate: document.getElementById("edit_departuredate").value
        };

        fetch('http://localhost:8090/api/booking/update/' + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + jwt_token
            },
            body: JSON.stringify(body)
        })
            .then(response => response.json())
            .then(json => {
                alert("Сохранено!");
                document.getElementById("booking_data").style.display = "none";
                show_bookings();
            });
    }

</script>
</body>
</html>