<?php  
//$url='https://www.p2pdi.com/appinterface/indexbanner.json';  
//$html = file_get_contents($url); 
//$interface='https://www.p2pdi.com/appinterface/indexbanner.json';

function getSslPage($url) {
$ch = curl_init();
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
curl_setopt($ch, CURLOPT_HEADER, false);
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_REFERER, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json;charset=utf-8'));

$result = curl_exec($ch);
curl_close($ch);
return $result;
}

    $myurl=$_GET["url"];
    
    //echo $url;
    $interface=str_replace('*',"&",$myurl);

echo getSslPage($interface);
?> 

