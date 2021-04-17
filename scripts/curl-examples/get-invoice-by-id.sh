#!/usr/bin/env bash

curl --request GET \
  --url http://localhost:8090/invoices/5 \
  --header 'authorization: Basic QWxpY2U6MTIz' \
  --header 'content-type: application/json' \
  --data '{"firstName": "Bilbo", "lastName": "Baggins", "description": "burglar"}'