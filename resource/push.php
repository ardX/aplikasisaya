<?php
function sendPushNotification($title, $message, $data1, $data2) {  
     
    $url = "https://fcm.googleapis.com/fcm/send";            
    $header = [
        'authorization: key=AAAAAgIm6Z4:APA91bHqAfV53LOozoZ5lumzphfXgljX6C4oMeUaCTjnFnTrkm74woaIdIDLtIsB4GU7hLUIhCImgNwewDyK6Te2x7Ult952k6ugsmDseZmfNYVSK6yodPUzkb71rpEwOrzXU5nWpjrb',
        'content-type: application/json'
    ];    
 
    $notification = [
        'title' =>$title,
        'body' => $message
    ];
	
    $data = [
        'data1' => $data1,
        'data2' => $data2,
        'ius' => $data2
    ];
	
    $fcmNotification = [
        'to'  => '/topics/mobile',
        'notification' => $notification,
        'data' => $data
    ];
 
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 60);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fcmNotification));
    curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
 
    $result = curl_exec($ch);    
    curl_close($ch);
 
    return $result;
}

echo sendPushNotification("Ini adalah judul notifikasi","Ini adalah isi dari notifikasi yang dikirim bla bla bla",123,456);

?>