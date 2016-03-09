$json = '{"group":{"name":"≈Û”—"}}';
$url = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=6j68MFz5WyxI2_xrdSj1XfjhZu7c3qTpgC8PrBznfqUbHe8kbaEe_mEDWZi2TdPYpAx9ljJNkmy1B6-A7dPe2y9L7RdwcjN2QKrhOydyOnveek111j7crIrNHzFzuKpii8VpFpizs7YGcwuangkEXg";
$result = https_post($url,$json);
var_dump($result);

function https_post($url,$data)
{
    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, $url); 
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE);
    curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, FALSE);
    curl_setopt($curl, CURLOPT_POST, 1);
    curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
    $result = curl_exec($curl);
    if (curl_errno($curl)) {
       return 'Errno'.curl_error($curl);
    }
    curl_close($curl);
    return $result;
}