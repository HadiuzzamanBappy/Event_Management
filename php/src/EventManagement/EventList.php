<?php 

require "Connection.php";

$response=array();

$mysql_qry="SELECT * from eventcreation";
    $result=mysqli_query($conn,$mysql_qry);
    while($row=$result->fetch_assoc()){
        array_push($response,array("id"=>$row['id'],"name"=>$row['title'],"lastdate"=>$row['EndRegistrationDate'],"eventStartdate"=>$row['EventStartDate'],"eventhoster"=>$row['EventHoster'],"EventhLength"=>$row['EventhLength']));
    }

    echo json_encode(array("Server_response"=>$response));

    mysqli_close($conn);

?>