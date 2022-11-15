<?php require_once('Connections/cnx.php'); ?>
<?php
	$Cedula=$_GET['cedula'];
	$Voto=1;
	$insertSQL = "INSERT INTO votos (cedula,voto)
	VALUES ('$Cedula', '$Voto')";
	mysqli_select_db($cnx,$database_cnx);
	$InsertVotos = mysqli_query($cnx,$insertSQL) or die(mysql_error());
	session_start();
	echo "<script type=\"text/javascript\">alert(\"VOTO REGISTRADO CON ÉXITO\");</script>"; 
	echo "<script type='text/javascript'>window.location='index.php'</script>";
?>
