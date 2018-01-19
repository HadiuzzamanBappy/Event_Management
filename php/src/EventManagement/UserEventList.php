<?php 

require "Connection.php";

$userid=$_POST["id"];

// $userid="1";

$response=array();

$mysql_qry="SELECT * from eventcreation where EventHoster like '$userid'";
    $result=mysqli_query($conn,$mysql_qry);
    while($row=$result->fetch_assoc()){
        array_push($response,array("id"=>$row['id'],"name"=>$row['title'],"lastdate"=>$row['EndRegistrationDate'],"eventStartdate"=>$row['EventStartDate'],"eventhoster"=>$row['EventHoster'],"EventhLength"=>$row['EventhLength']));
    }

    echo json_encode(array("Server_response"=>$response));

    mysqli_close($conn);

?>