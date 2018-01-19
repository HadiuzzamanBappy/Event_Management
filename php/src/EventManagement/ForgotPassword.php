<?php 

require "Connection.php";

$email=$_POST["email"];

// $email="bappy79@gmail.com";

$mysql_qry="SELECT * from userinformation where email like '$email'";
$result=mysqli_query($conn,$mysql_qry);
if($row=$result->fetch_assoc()){
    echo $row['recoveryValue'];	
}
else
{
    echo "no";
}
mysqli_close($conn);

?>