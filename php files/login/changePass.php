<?php

 if($_SERVER['REQUEST_METHOD']=='GET'){

 include 'DatabaseConfig.php';
 
 $con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);
 
 $id = $_GET['id'];
 //$password = $_POST['password'];
 
 $random = rand(10,100);
 $Sql_Query = "UPDATE park.UserLoginTable SET pass='$random' WHERE id='1' ";
 $Sql_Query1 = "UPDATE park.parking SET status=0 WHERE id='1' ";
 
 $check = (mysqli_query($con,$Sql_Query));
 
 if(isset($check)){
 
 mysqli_query($con,$Sql_Query);
 echo "Password Changed";
 }
 else{
 echo "Invalid Username or Password Please Try Again";
 }
 
 }else{
 echo "Check Again";
 }
mysqli_close($con);

?>