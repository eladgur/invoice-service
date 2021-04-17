#!/usr/bin/env bash

curl --request PUT \
  --url http://localhost:8090/invoices/schedule/5 \
  --header 'authorization: Basic QWxpY2U6MTIz' \
  --header 'content-type: application/json' \
  --data '{
	"scheduledDate": "2021-05-05"
}'