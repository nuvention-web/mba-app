<?php

include('./MailChimp.php');
use \DrewM\MailChimp\MailChimp;

// Create connection
$conn = new mysqli("mysql.myapp.mba", "myappmba", "AkkN42SynDqy7wx", "myappmba");
// Check connection
if ($conn->connect_error) {
  http_response_code(500);
	$response = array(
		"status" => "failure",
		"text" => "Database connection error."
	);
	echo json_encode($response);
	die();
}

// variables
$fname = $conn->escape_string($_POST["fname"]);
$lname = $conn->escape_string($_POST["lname"]);
$email = $conn->escape_string(strtolower($_POST["email"]));
$dob = $conn->escape_string($_POST["dob"]);
$occ = $conn->escape_string($_POST["occ"]);
$signup_ip_address = $conn->escape_string($_SERVER['REMOTE_ADDR']);
$utm_source = $conn->escape_string(isset($_POST["utm_source"]) ? $_POST["utm_source"] : "");
$utm_name = $conn->escape_string(isset($_POST["utm_name"]) ? $_POST["utm_name"] : "");
$utm_term = $conn->escape_string(isset($_POST["utm_term"]) ? $_POST["utm_term"] : "");
$utm_content = $conn->escape_string(isset($_POST["utm_content"]) ? $_POST["utm_content"] : "");
$utm_medium = $conn->escape_string(isset($_POST["utm_medium"]) ? $_POST["utm_medium"] : "");
$hash = md5(strtolower($_POST["email"]));

// Send to database
$sql = "REPLACE INTO signups (email, fname, lname, dob, occ, utm_source, utm_medium, utm_name, utm_term, utm_content, signup_ip_address, complete)
VALUES ('$email', '$fname', '$lname', '$dob', '$occ', '$utm_source', '$utm_medium', '$utm_name', '$utm_term', '$utm_content', '$signup_ip_address', 1)";
if ($conn->query($sql) !== TRUE) {
	header('Content-Type: application/json');
	http_response_code(400);
	$response = array(
		"status" => "failure",
		"text" => $conn->error
	);
	echo json_encode($response);
	die();
}
$conn->close();

// Send to Mailchimp
$MailChimp = new MailChimp('a5efe4896313c02211a6e1369e17450a-us18');
$result = $MailChimp->put("lists/fb2c2ea098/members/$hash", [
				'email_address' => $email,
				'status_if_new' => 'subscribed',
        'merge_fields' => ['FNAME'=>$fname, 'LNAME'=>$lname, 'DOB'=>$dob, 'OCC'=>$occ],
        'ip_signup' => $signup_ip_address,
			]);

// Error handling
if($s = $MailChimp->getLastError()) {
	header('Content-Type: application/json');
	http_response_code(400);
	$response = array(
		"status" => "failure",
		"text" => $s
	);
	echo json_encode($response);
  print_r($MailChimp->getLastRequest());

	die();
}

// Successful registration
header('Content-Type: application/json');
http_response_code(200);
$response = array(
	"status" => "success",
);
echo json_encode($response);


?>
