<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://w3.org">
<html>
<head>
    <meta charset="UTF-8">
    <title>Тест</title>
    <style>
        #booking_table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        #booking_table th, #booking_table td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        /* Стиль для всей строки, чтобы она выглядела кликабельной */
        .booking-row {
            cursor: pointer;
            transition: background-color 0.2s;
        }
        /* Подсветка при наведении */
        .booking-row:hover {
            background-color: #f0f8ff;
        }
    </style>
</head>
<body>

<form id="form" method="post" action="/app">
    <div><input name="username" type="text" placeholder="username"/></div>
    <div><input name="password" type="password" placeholder="password"/></div>
    <div><input type="button" value="OK" onclick="login()"/></div>
</form>
<div id="error_box" style="display:none;color:red;margin-top:10px;"></div>

<div id="content_section" style="display:none">
    <h3>Данные по бронированию в гостинице:</h3>
    <div>
        <table id="booking_table">
            <thead>
                <tr>
                    <th>Комната</th>
                    <th>Дата заезда</th>
                    <th>Дата проживания</th>
                </tr>
            </thead>
            <tbody id="booking_body"></tbody>
        </table>
    </div>
</div>

<div id="booking_data" style="display:none; margin-top:20px; border-top: 2px solid #ccc; pt: 10px;">
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

    function showError(message) {
        const box = document.getElementById('error_box');
        box.style.display = 'block';
        box.textContent = message;
    }

    function clearError() {
        const box = document.getElementById('error_box');
        box.style.display = 'none';
        box.textContent = '';
    }

    async function login() {
        clearError();
        const formElement = document.getElementById('form');
        const formData = new FormData(formElement);
        const jsonData = JSON.stringify(Object.fromEntries(formData));

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json;charset=utf-8' },
                body: jsonData
            });

            const json = await response.json();

            if (!response.ok || !json.token) {
                showError(json.message || 'Не удалось войти.');
                return;
            }

            jwt_token = json.token;
            document.getElementById('form').style.display = 'none';
            document.getElementById('content_section').style.display = 'block';
            await show_bookings();
        } catch (e) {
            showError("Ошибка сети или сервера");
        }
    }

    async function show_bookings() {
        const response = await fetch('/api/booking/allview', {
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + jwt_token }
        });

        if (!response.ok) {
            showError('Не удалось получить список бронирований.');
            return;
        }

        const json = await response.json();
        make_booking_table(json);
    }

        function make_booking_table(data) {
            let tbody = document.getElementById('booking_body');
            tbody.innerHTML = '';

            const bookings = data.bookings || [];

            bookings.forEach(booking => {
                let tr = document.createElement('tr');
                tr.className = 'booking-row';
                tr.onclick = () => open_booking(booking.id);

                <#noparse>
                tr.innerHTML = `
                    <td>${booking.room}</td>
                    <td>${booking.arrivaldate}</td>
                    <td>${booking.stayingdate}</td>
                `;
                </#noparse>

                tbody.append(tr);
            });
        }


    async function open_booking(booking_id) {
        const response = await fetch('/api/booking/get/' + booking_id, {
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + jwt_token }
        });

        if (!response.ok) {
            showError('Ошибка загрузки данных.');
            return;
        }

        const json = await response.json();
        fill_booking(json);
    }

    function fill_booking(data) {
        document.getElementById('edit_id').value = data.id;
        document.getElementById('edit_room').value = data.room || '';
        document.getElementById('edit_arrivaldate').value = formatDate(data.arrivaldate);
        document.getElementById('edit_stayingdate').value = formatDate(data.stayingdate);
        document.getElementById('edit_departuredate').value = formatDate(data.departuredate);
        document.getElementById('edit_name').value = data.name || '';
        document.getElementById('edit_gender').value = data.gender || '';
        document.getElementById('booking_data').style.display = 'block';
    }

    function formatDate(dateVal) {
        if (!dateVal) return '';
        return new Date(dateVal).toISOString().split('T')[0];
    }

    async function saveBooking() {
        const id = document.getElementById('edit_id').value;
        const body = {
            id: parseInt(id),
            room: document.getElementById('edit_room').value,
            arrivaldate: document.getElementById('edit_arrivaldate').value,
            stayingdate: document.getElementById('edit_stayingdate').value,
            departuredate: document.getElementById('edit_departuredate').value
        };

        const response = await fetch('/api/booking/update/' + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + jwt_token
            },
            body: JSON.stringify(body)
        });

        if (!response.ok) {
            showError('Ошибка при сохранении.');
            return;
        }

        alert('Сохранено!');
        document.getElementById('booking_data').style.display = 'none';
        await show_bookings();
    }
</script>
</body>
</html>
