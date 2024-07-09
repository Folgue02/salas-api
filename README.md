# Sample REST API

This is a REST API for managing and creating meeting rooms and bookings for them.

## 1. Requirements

### 1.1 Server

- Docker
- Java 17+

### 1.2 Client script

For the client script `Python 3` is required as well as the following pip modules:

- [PyInquirer](https://pypi.org/project/PyInquirer/)
- [colorama](colorama)
- [termcolor](https://pypi.org/project/termcolor/)

Check the [requirements.txt](requirements.txt) file to install them all with `pip install -r requirements.txt`.

## 2. Deployment

To start the server you first have to package it with maven:

```bash
mvn package
```

And then you can use `docker-compose` to build the image and start the database.
```bash
docker-compose up -d
```

## 3. Routes

This API uses two main route prefixes:
- `/reservas` to manage the bookings.
- `/salas` to manage the rooms.

### 3.1 Room management

| **Endpoint**  | **Method** | **Request params**                          | **Returns**                                                    | **Errors**                                                                                                                        |
|---------------|------------|---------------------------------------------|----------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| `/salas/`     | `GET`      | *None*                                      | A list containing all the room objects stored in the database. | *None*                                                                                                                            |
| `/salas/{id}` | `GET`      | *None*                                      | The object of the room with the specified id.                  | `404` is returned if the room doesn't exist.                                                                                      |
| `/salas/`     | `POST`     | `name`, `capacity` and `location`           | The object of the room that has been created.                  | `400`, if the capacity is < 0 or the location doesnt follow the format of character + number.                                     |
| `/salas/`     | `PUT`      | `name`, `capacity`, `location` and `roomId` | The object of the room after the update.                       | `400`, if the capacity is < 0 or the location doesnt follow the format of character + number.<br>`404` if the room doesn't exist. |

### 3.2 Booking management

| **Endpoint**     | **Method** | **Request params**                               | **Returns**                                                       | **Errors**                                                                                                                                                 |
|------------------|------------|--------------------------------------------------|-------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/reservas/`     | `GET`      | *None*                                           | A list containing all the booking objects stored in the database. | *None*                                                                                                                                                     |
| `/reservas/{id}` | `GET`      | *None*                                           | The object of the booking with the specified id.                  | `404` is returned if the booking doesn't exist.                                                                                                            |
| `/reservas/`     | `POST`     | `organizer`, `startDate`, `endDate` and `roomId` | The object of the booking that has been created.                  | `404`, if the there is no room with such ID.<br>`400` if the format of the date is invalid.<br>`409` if the room is already booked for the time range.     |
| `/reservas/{id}` | `PUT`      | `organizer`, `startDate`, `endDate` and `roomId` | The object of the booking after the update.                       | `400`, if the format of the date is invalid.<br>`404` if the room or the booking doesn't exist.<br>`409` if the room is already booked for the time range. |
| `/reservas/{id}` | `DELETE`   | *None*                                           | The object of the booking that has been removed.                  | `404` if the booking doesn't exist.                                                                                                                        |

## 4. Client script

This repository comes with a python script to test the API, which is located at `scripts/full-test`.

On Linux/Mac:
```bash
scripts/full-test
```

On Windows:
```bash
python scripts/full-test
```