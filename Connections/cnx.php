<?php
	$hostname = 'localhost';
	$username = 'root';
	$password = '';
	$database_cnx = 'elecciones2020';
	$cnx = mysqli_connect($hostname, $username, $password, $database_cnx) or trigger_error("Error en base de datos",E_USER_ERROR);
	mysqli_set_charset($cnx, "utf8")
?>