<?php
require "Connection.php";

$email=$_POST["email"];
$pass=$_POST["password"];

// $email="hbappy79@gmail.com";
// $pass="bappy";

$mysql_qry="SELECT * FROM userinformation where email like '$email' and password like '$pass'";
$result=mysqli_query($conn,$mysql_qry);

$response=array();

if($row=$result->fetch_assoc())
{
    //echo "ok";
    array_push($response,array("id"=>$row['id'],"username"=>$row['username'],"phone"=>$row['phone'],"city"=>$row['city']));
    echo json_encode(array("Server_response"=>$response));
}
else
    echo "Wrong Information... Please Input correct USERNAME(exact) And PASSWORD";

mysqli_close($conn);
?>