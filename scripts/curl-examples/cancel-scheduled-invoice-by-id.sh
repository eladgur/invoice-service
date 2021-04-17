#!/usr/bin/env bash

curl --request DELETE \
  --url http://localhost:8090/invoices/schedule/5 \
  --header 'authorization: Basic QWxpY2U6MTIz' \
  --header 'content-type: application/json' \
  --data '{
	"invoiceId": "5",
	"amount": 14,
	"creationDate": "1991-12-23",
	"companyName": "Asd",
	"customerEmail": "a"
}'
