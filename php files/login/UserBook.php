<?php
//$id = (isset($_GET['id']) ? $_GET['id'] : '');
 if($_SERVER['REQUEST_METHOD']=='POST'){

 include 'connection.php';
 
 $conn = mysqli_connect($server_name,$mysql_username,$mysql_password,$db_name);
 
 $id = (isset($_POST['email']) ? $_POST['email'] : '');
 $id1 = (isset($_POST['password']) ? $_POST['password'] : '');
 //$password = $_POST['password'];
 
 $Sql_Query = "UPDATE park.parking SET status=1 WHERE id='$id' ";
 $random = rand(10,100);
 $Sql_Query1 = "UPDATE park.UserLoginTable SET pass='$random' WHERE id='1' ";
 
// $check = mysqli_fetch_array(mysqli_query($con,$Sql_Query));
 
 if(mysqli_query($conn, $Sql_Query)){
 //echo "${id}";
	mysqli_query($conn, $Sql_Query1);
    echo "Records were updated successfully.";
} else {
    echo "ERROR: Could not able to execute $sql. " . mysqli_error($link);
}
mysqli_close($conn);
 }
// Close connection
//mysqli_close($link);
  

?>