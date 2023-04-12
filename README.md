# Spring boot rabbit retry example

## Build

To build docker image with application execute `mvn clean package -P build-docker`

## Launch

You can launch everything with docker-compose:

```bash
docker-compose up -d
```

Or launch rabbit using docker compose 

```bash
docker-compose up -d rabbit
```

And application using Maven `mvn spring-boot:run` or IDE.

## Interact

The service have two simple endpoints to check:

```
curl -X PUT http://localhost:8091/text --json "{\"text\": \"do fail\"}"
```

Send some text over rabbit, consumes it from a queue and put to internal variable to return in the method below.
If you send text "do fail", then special logic launches that fails message handling by consumer.
Obviously, the text wouldn't be updated, however, you would see that it was moved to parking lot (see below).

```
GET http://localhost:8091/text
```

Returns saved text value

## Check rabbit queues

1. Go to rabbit manager http://localhost:15672.
2. Login with guest/guest.
3. Open **Queues** tab.
4. Click on **parking-lot.exchange.queue**.
5. And there you can click **Get messages** to check message content/
