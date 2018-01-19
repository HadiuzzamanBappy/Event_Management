<?php 

require "Connection.php";

$name=$_POST["username"];
$email=$_POST["email"];
$city=$_POST["city"];
$phone=$_POST["phone"];
$password=$_POST["password"];

// $name="aakash";
// $email="a@gmail.com";
// $city="dhaka";
// $phone="0123";
// $password="123";

$mysql_qry="SELECT * from userinformation where email like '$email'";
    $result=mysqli_query($conn,$mysql_qry);
    if($row=$result->fetch_assoc()){
        echo "Email Already Exist....Sorry";
    }
    else{
    	$random=floor(rand()*10-1);
    $mysql_qry3="INSERT into userinformation(username,email,phone,password,city,recoveryValue)
                        			values('$name','$email','$phone','$password','$city','$random')";
     if($result=mysqli_query($conn,$mysql_qry3))
        {
            $mysql_qry="SELECT * from userinformation where email like '$email'";
            $result=mysqli_query($conn,$mysql_qry);
            if($row=$result->fetch_assoc()){
            echo $row['id'];
            }
        }
}

?>