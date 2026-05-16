import requests

with open ("input.jpg", "rb") as f:
    files = {"image": f}

    r1 = requests.post ("http://localhost:5000/grayscale", files=files)
    with open ("out_gray.jpg", "wb") as out: out.write (r1.content)

    r2 = requests.post ("http://localhost:5000/resize", files=files)
    with open ("out_resize.jpg", "wb") as out: out.write (r2.content)