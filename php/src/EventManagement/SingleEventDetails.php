<?php 

require "Connection.php";

$id=$_POST["id"];

// $id="1";

$response=array();

$mysql_qry="SELECT * from eventcreation where id like '$id'";
    $result=mysqli_query($conn,$mysql_qry);
    while($row=$result->fetch_assoc()){
    	$userid=$row['EventHoster'];
    	
    	$mysql_qry2="SELECT * from userinformation where id like '$userid'";
    		$result2=mysqli_query($conn,$mysql_qry2);
    			$row2=$result2->fetch_assoc();
    				$hostername=$row2['username'];
    				$hosteremail=$row2['email'];

        array_push($response,array("title"=>$row['title'],"hostername"=>$hostername,"hosteremail"=>$hosteremail,"StartRegistrationDate"=>$row['StartRegistrationDate'],"EndRegistrationDate"=>$row['EndRegistrationDate'],"EventStartDate"=>$row['EventStartDate'],"OrganizedBy"=>$row['OrganizedBy'],"Venue"=>$row['Venue'],"ContactNo"=>$row['ContactNo'],"EventhLength"=>$row['EventhLength'],"Fees"=>$row['Fees'],"about"=>$row['about']));
    }

    echo json_encode(array("Server_response"=>$response));

    mysqli_close($conn);

?>