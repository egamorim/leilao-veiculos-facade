## create

curl --request POST \
  --url http://localhost:8080/v1/auction/vehicle \
  --header 'content-type: application/json' \
  --data '{
	"bid": {
		"date": "21/08/2020 - 13:24",
		"value": 15000.02,
		"user" : {
			"name" : "Eduardo Amorim"
		}
	},
	"lot": {
		"name": "00001",
		"controlCode": "80"
	},
	"manufacturer": {
		"name": "Ford"
	},
	"model": {
		"name": "Focus",
		"year": 2001
	},
	"manufactureYear": 2002
}'

## Delete

curl --request DELETE \
  --url http://localhost:8080/v1/auction/vehicle/15 \
  --header 'content-type: application/json'

## Update

curl --request PUT \
  --url http://localhost:8080/v1/auction/vehicle/8 \
  --header 'content-type: application/json' \
  --data '{
	"bid": {
		"date": "21/08/2020 - 13:24",
		"value": 15000.02,
		"user" : {
			"name" : "Eduardo"
		}
	},
	"lot": {
		"name": "00001",
		"controlCode": "80"
	},
	"manufacturer": {
		"name": "Ford"
	},
	"model": {
		"name": "Focus",
		"year": 2001
	},
	"manufactureYear": 2002
}'

## Get a vehicle by id
curl --request GET \
  --url http://localhost:8080/v1/auction/vehicle/12 \
  --header 'content-type: application/json'
  
## Get all

curl --request GET \
  --url 'http://localhost:8080/v1/auction/vehicle?offset=0&sort=desc' \
  --header 'content-type: application/json'

## Find by lot

curl --request GET \
  --url 'http://localhost:8080/v1/auction/vehicle/lot/9999?offset=3&sort=desc' \
  --header 'content-type: application/json'

## Find by the manufacturer' name

curl --request GET \
  --url 'http://localhost:8080/v1/auction/vehicle/manufacturer/ford?offset=58&sort=asc' \
  --header 'content-type: application/json'

## Find by the beginning of the model' name

curl --request GET \
  --url 'http://localhost:8080/v1/auction/vehicle/model?startsWith=m&offset=3&sort=desc' \
  --header 'content-type: application/json'

## Find by the manufacture year and model year 

curl --request GET \
  --url 'http://localhost:8080/v1/auction/vehicle/manufacture/2007/model/2008?offset=60&sort=desc' \
  --header 'content-type: application/json'

## Find by a manufacture period

curl --request GET \
  --url 'http://localhost:8080/v1/auction/vehicle/manufacture?yearFrom=2010&yearTo=2012&offset=0&sort=desc' \
  --header 'content-type: application/json'

