<?php

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

$email = $conn->escape_string(strtolower($_POST["email"]));
$utm_source = $conn->escape_string($_POST["utm_source"]);
$utm_name = $conn->escape_string($_POST["utm_name"]);
$utm_term = $conn->escape_string($_POST["utm_term"]);
$utm_content = $conn->escape_string($_POST["utm_content"]);
$utm_medium = $conn->escape_string($_POST["utm_medium"]);
$signup_ip_address = $conn->escape_string($_SERVER['REMOTE_ADDR']);

$sql = "INSERT INTO signups (email, utm_source, utm_medium, utm_name, utm_term, utm_content, signup_ip_address, complete)
VALUES ('$email', '$utm_source', '$utm_medium', '$utm_name', '$utm_term', '$utm_content', '$signup_ip_address', 0)";

if ($conn->query($sql) === TRUE) {
  echo "New record created successfully.";
} else {
  http_response_code(500);
}

$conn->close();

?>
