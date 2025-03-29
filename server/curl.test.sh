curl  -H 'Content-Type: application/json' -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJtZW1iZXIuaW5mby5wdWJsaWMiXSwiZXhwIjoxNTY2MDM3MDc0LCJhdXRob3JpdGllcyI6WyJST0xFX01ZX0NMSUVOVCJdLCJqdGkiOiI0OWFhNjU5NC05ODg4LTQ2OWUtYWFhMi02YjcyNTQxZWZlMjMiLCJjbGllbnRfaWQiOiJteV9jbGllbnRfaWQifQ.fzjwaElVtCLRJ5_4MHds98jgssS6s5YpX9jgWCnQi-s"  -X POST  -d '[{"lat":"34.781676","lng":"126.822107","name":"point0","type":"car"},{"lat":"35.271984","lng":"128.393152","name":"point1","type":"car"},{"lat":"35.884056","lng":"127.228602","name":"point2","type":"car"},{"lat":"34.894392","lng":"126.772669","name":"point3","type":"car"}]' "http://localhost:8081/api/new/cp"






curl  -H 'Content-Type: application/json' -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJtZW1iZXIuaW5mby5wdWJsaWMiXSwiZXhwIjoxNTY2MDM3MDc0LCJhdXRob3JpdGllcyI6WyJST0xFX01ZX0NMSUVOVCJdLCJqdGkiOiI0OWFhNjU5NC05ODg4LTQ2OWUtYWFhMi02YjcyNTQxZWZlMjMiLCJjbGllbnRfaWQiOiJteV9jbGllbnRfaWQifQ.fzjwaElVtCLRJ5_4MHds98jgssS6s5YpX9jgWCnQi-s"  -X POST  -d '[{"lat":"37.52463","lng":"126.909104","name":"point0","type":"car"},{"lat":"36.784743","lng":"127.183763","name":"point1","type":"car"},{"lat":"35.810778","lng":"128.546067","name":"point2","type":"car"},{"lat":"37.685649","lng":"128.859178","name":"point3","type":"car"}]' "http://localhost:8081/api/new/cp"


[{"lat":"37.52463","lng":"126.909104","name":"point0","type":"car"},{"lat":"36.784743","lng":"127.183763","name":"point1","type":"car"},{"lat":"35.810778","lng":"128.546067","name":"point2","type":"car"},{"lat":"37.685649","lng":"128.859178","name":"point3","type":"car"}]



curl -d "client_id=my_client_id&client_secret=my_client_secret&grant_type=client_credentials&scope=member.info.public" -X POST "http://my_client_id:my_client_secret@api.pizzastudio.app/oauth2-server/oauth/token"


curl  -H 'Content-Type: application/json' -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJtZW1iZXIuaW5mby5wdWJsaWMiXSwiZXhwIjoxNTY2MDQ1NDUwLCJhdXRob3JpdGllcyI6WyJST0xFX01ZX0NMSUVOVCJdLCJqdGkiOiI0MjI4OGIwMC1kZjE0LTQxODctODllZC0wZmYzMDcxMTdkNjAiLCJjbGllbnRfaWQiOiJteV9jbGllbnRfaWQifQ.AG7klkpTsM5OaDLdmGMACZ2r3hT-wLBr83kO7FOkOiA"  -X POST  -d '[{"lat":"37.52463","lng":"126.909104","name":"point0","type":"car"},{"lat":"36.784743","lng":"127.183763","name":"point1","type":"car"},{"lat":"35.810778","lng":"128.546067","name":"point2","type":"car"},{"lat":"37.685649","lng":"128.859178","name":"point3","type":"car"}]' "http://api.pizzastudio.app/api-server/api/new/cp"