<?php 

require "Connection.php";

$event_id=$_POST["event_id"];

// $event_id="1";

$response=array();

$mysql_qry="SELECT * from eventuseradd where eventid like '$event_id'";
    $result=mysqli_query($conn,$mysql_qry);
    while($row=$result->fetch_assoc()){
    	$userid=$row['userid'];
    	$mysql_qry2="SELECT * from userinformation where id like '$userid'";
    	$result2=mysqli_query($conn,$mysql_qry2);
    	if($row2=$result2->fetch_assoc()){
        array_push($response,array("name"=>$row2['username'],"email"=>$row2['email'],"organization"=>$row['organization'],"phone"=>$row['contactno'],"city"=>$row2['city'],"paymethod"=>$row['paymentmethod']));
    }
 }

    echo json_encode(array("Server_response"=>$response));

    mysqli_close($conn);

?>