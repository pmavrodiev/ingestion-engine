#THIS IS A REST POST TEST

$Url = "http://localhost:8081/topics/avrotest"

$headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
$headers.Add("Content-Type", 'application/vnd.kafka.binary.v1+json')

$Body = @{
		records =@(@{value="S2Fma2E="})
	}

Invoke-RestMethod -Method Post -Uri $url -Body $body  -Headers $headers

