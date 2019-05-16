Для запуска:

Либо скачайте джарник из раздела релизов и выполните шаг `3`

либо если хотите собрать с любой ветки:

1. `mvn clean package` 

2. `cd target`

3. `java -jar accounts-<version>.jar`

где `version` - версия артефакта

Пример запросов
 
`http://localhost:8080/addAccount` (POST)

>{
 	"name": "Jack",
 	"sum": 1000
 }

Успешный твет

>{
     "success": true,
     "body": {
         "id": 2,
         "name": "Jack",
         "sum": 1000
     }
 }

`http://localhost:8080/getAllAccounts`(GET)

Успешный твет

>{
     "success": true,
     "body": [
         {
             "id": 1,
             "name": "Jack",
             "sum": 450
         },
         {
             "id": 2,
             "name": "Jack",
             "sum": 1550
         }
     ]
 }
 
 `http://localhost:8080/transferMoney` (POST)
 где `from` и `to` - id соответствующих счетов
 
 >{
  	"amount": 550,
  	"from": 1,
  	"to": 2
  }
  
Успешный ответ

>{
     "success": true
 }

Ответ с ошибкой

>{
     "success": false,
     "error": "Error getting account by id 1"
 }

