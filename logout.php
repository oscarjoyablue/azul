<?php 
	//to fully log out a visitor we need to clear the session variables
	session_start();
	//$_SESSION['Admin'] = NULL;
	$_SESSION['MM_Username'] = NULL;
	$_SESSION['MM_TipoUser'] = NULL;
	$_SESSION['MM_Nombre'] = NULL;
	//$_SESSION['PrevUrl'] = NULL;
	$_SESSION["autentificado"]=NULL;
	//$_SESSION["cedula"]=NULL;
	$_SESSION["autentificado"]="NO";
	unset($_SESSION['MM_Username']);
	unset($_SESSION['MM_TipoUser']);
	unset($_SESSION['MM_Nombre']);
	//unset($_SESSION['PrevUrl']);
	//unset($_SESSION['cedula']);
	$logoutGoTo = "index.php";
	header("Location: $logoutGoTo");
?>