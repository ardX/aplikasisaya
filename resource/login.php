<?php
define("MYSQL_DB_HOST", "localhost");
define("MYSQL_DB_USER", "admin");
define("MYSQL_DB_PASSWORD", "mobile");
define("MYSQL_DB_DATABASE", "aplikasiku");
$conn = mysqli_connect(MYSQL_DB_HOST, MYSQL_DB_USER, MYSQL_DB_PASSWORD, MYSQL_DB_DATABASE);
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$user = mysqli_real_escape_string($conn, $_POST['user']);
$password = mysqli_real_escape_string($conn, $_POST['password']);

$sql = "SELECT * FROM pengguna WHERE username='{$user}'";
$query = mysqli_query($conn, $sql) or die("FAILED");
if(mysqli_num_rows($query) > 0){
    $row = mysqli_fetch_array($query);
    if($row['password']==$password){
        $user = $row['username'];
        $email = $row['email'];
        $ponsel = $row['ponsel'];
        die($user.'|'.$email.'|'.$ponsel);
    }
    die("FAILED");
}else{
    die("FAILED");
}
die("FAILED");
?>