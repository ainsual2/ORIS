<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Тест</title>
</head>
<body>

<h1>Main page</h1>

<h2>Bookings:</h2>
<div id="bookings"></div>

<script>
    let jwt_token = "${jwt_token!}"

    async function loadBookings() {
        try {
            const response = await fetch('/api/booking/allview', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + jwt_token,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const data = await response.json();
                const bookingsDiv = document.getElementById('bookings');

                if (data.bookings && data.bookings.length > 0) {
                    let html = '<table border="1"><tr><th>ID</th><th>Name</th><th>Room</th><th>Arrival Date</th><th>Staying Date</th></tr>';
                    data.bookings.forEach(booking => {
                        html += `<tr>
                            <td>${booking.id}</td>
                            <td>${booking.name}</td>
                            <td>${booking.room}</td>
                            <td>${booking.arrivaldate}</td>
                            <td>${booking.stayingdate}</td>
                        </tr>`;
                    });
                    html += '</table>';
                    bookingsDiv.innerHTML = html;
                } else {
                    bookingsDiv.innerHTML = '<p>No bookings found</p>';
                }
            } else {
                document.getElementById('bookings').innerHTML = '<p>Error loading bookings: ' + response.status + '</p>';
            }
        } catch (error) {
            document.getElementById('bookings').innerHTML = '<p>Error: ' + error.message + '</p>';
        }
    }

    // Load bookings when page loads
    loadBookings();
</script>

</body>
</html>