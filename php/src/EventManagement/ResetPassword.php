<?php
require "Connection.php";

$id=$_POST["id"];
$password=$_POST["password"];

$mysql_qry10="UPDATE userinformation SET password = '$password' WHERE id= '$id'";
  	if ($conn->query($mysql_qry10) === TRUE)
    	echo "Password Updated.";

?>