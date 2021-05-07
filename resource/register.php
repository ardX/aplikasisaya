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
$email = mysqli_real_escape_string($conn, $_POST['email']);
$ponsel = mysqli_real_escape_string($conn, $_POST['ponsel']);
$password = mysqli_real_escape_string($conn, $_POST['password']);

if(!empty($user)&&!empty($email)&&!empty($ponsel)&&!empty($password)){
	$sql = "INSERT INTO pengguna(username, email, ponsel, password) VALUES ('{$user}', '{$email}', '{$ponsel}', '{$password}');";
	$query = mysqli_query($conn, $sql) or die("Pengguna atau email sudah terdaftar!");
	die("OK");
}
else {
    die("Semua isian tidak boleh kosong!");
}
die("FAILED");
?>