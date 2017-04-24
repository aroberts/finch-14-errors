## Reproduction of error serialization issue in Finch

https://github.com/finagle/finch/issues/765

### Run server

    sbt rest/run

### Make requests

    http :5000/ping #=> renders a json object in a 200 response
    http :5000/error #=> renders an empty 400
    http :5000/error?text=foo #=> renders "foo" in a 200 response
