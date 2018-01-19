<?php 

require "Connection.php";

$organization=$_POST["organization"];
$contactNo=$_POST["contactNo"];
$paymentmethod=$_POST["paymentmethod"];
$eventid=$_POST["eventid"];
$userid=$_POST["userid"];

// $organization="KU";
// $contactNo="01956324589";
// $paymentmethod="bkash";
// $eventid="5";
// $userid="5";

$mysql_qry="SELECT * from eventuseradd where eventid like '$eventid' and userid like '$userid'";
    $result=mysqli_query($conn,$mysql_qry);
    if($row=$result->fetch_assoc()){
        echo "You Already Registered In This Event...Sorry";
    }
    else{
    $mysql_qry3="INSERT into eventuseradd(organization,contactNo,paymentmethod,eventid,userid)
                values('$organization','$contactNo','$paymentmethod','$eventid','$userid')";
     if($result=mysqli_query($conn,$mysql_qry3))
        {
            echo "Registration Completed.Thank You For Associating with Us";
        }
}