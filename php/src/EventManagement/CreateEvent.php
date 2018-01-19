<?php 

require "Connection.php";

$title=$_POST["title"];
$id=$_POST["id"];
$registrationstart=$_POST["registrationstart"];
$registrationend=$_POST["registrationend"];
$eventstart=$_POST["eventstart"];
$eventlength=$_POST["eventlength"];
$organizedby=$_POST["organizedby"];
$venue=$_POST["venue"];
$phone=$_POST["phone"];
$fees=$_POST["fees"];
$about=$_POST["about"];

// $title="See Happening";
// $id="1";
// $registrationstart="2017-12-12";
// $registrationend="2017-12-15";
// $eventstart="201-12-17";
// $eventlength="2";
// $organizedby="Bappy";
// $venue="khulna";
// $phone="01932089409";
// $fees="300";
// $about="this is demo event";

$mysql_qry="SELECT * from eventcreation where title like '$title'";
    $result=mysqli_query($conn,$mysql_qry);
    if($row=$result->fetch_assoc()){
        echo "Event Already Exist.Change Event Name.";
    }
    else{
    $mysql_qry3="INSERT into eventcreation(title,EventHoster,StartRegistrationDate,EndRegistrationDate,EventStartDate,OrganizedBy,Venue,ContactNo,EventhLength,Fees,about)
                values('$title','$id','$registrationstart','$registrationend','$eventstart','$organizedby','$venue','$phone','$eventlength','$fees','$about')";
     if($result=mysqli_query($conn,$mysql_qry3))
        {
            echo "Event Created.Thank You For Associating with Us";
        }
}