<?php require_once('Connections/cnx.php'); ?>
<?php
    mysqli_select_db($cnx,$database_cnx);
    $query_RstCandidatos = "SELECT * FROM candidatos";
    $RstCandidatos = mysqli_query($cnx,$query_RstCandidatos) or die(mysql_error());
    $row_RstCandidatos = mysqli_fetch_assoc($RstCandidatos);
    $totalRows_RstCandidatos = mysqli_num_rows($RstCandidatos);
    //
     function nombrecategoria($Id)
    {
    require('Connections/cnx.php'); 
    $sql1 = "SELECT * FROM categorias WHERE idcategoria=$Id";
    $RstCategorias = mysqli_query($cnx,$sql1) or die(mysql_error());
    $row_Categorias = mysqli_fetch_assoc($RstCategorias);
    $totalRows_RstCategorias = mysqli_num_rows($RstCategorias);
    $Nombre = '';
    $Nombre =$row_Categorias['categoria'];
    return $Nombre;  
    }
    //fin
?>    
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Elecciones Master 2021</title>
	<link href="https://fonts.googleapis.com/css?family=Montserrat:400,700%7CVarela+Round" rel="stylesheet">
	<link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="css/owl.carousel.css" />
	<link type="text/css" rel="stylesheet" href="css/owl.theme.default.css" />
	<link type="text/css" rel="stylesheet" href="css/magnific-popup.css" />
	<link rel="stylesheet" href="css/font-awesome.min.css">
	<link type="text/css" rel="stylesheet" href="css/style.css" />
	<link rel="shortcut icon" href="assets/images/favicon.ico">
	<style type="text/css">
    #imagen {
        height: 150px;
        width: 150px;
        border-radius: 60px;
        border-style: solid;
        border-width: 1px;
        border-color: #f4b906;
        padding: 2px;
    }
</style>  
</head>
<body>
	<!-- Header -->
	<header id="home">
		<!-- Background Image -->
		<div class="bg-img" style="background-image: url('./images/vote.jpg'); height: 100%">
			<div class="overlay"></div>
		</div>
		<!-- /Background Image -->

		<!-- Nav -->
		<nav id="nav" class="navbar nav-transparent">
			<div class="container">
				<div class="navbar-header">
					<!-- Logo -->
					<div class="navbar-brand">
						<a href="index.html">
							<!-- <img class="logo" src="img/mesas.jpg" alt="logo">
							<img class="logo-alt" src="img/mesas.jpg" alt="logo"> -->
						</a>
					</div>
					<!-- /Logo -->
					<!-- Collapse nav button -->
					<div class="nav-collapse">
						<span></span>
					</div>
					<!-- /Collapse nav button -->
				</div>
				<!--  Main navigation  -->
				<ul class="main-nav nav navbar-nav navbar-right">
					<li><a href="#home">INICIO</a></li>
					<li><a href="#about">ACERCA</a></li>
					<li><a href="#pricing">VOTACIONES</a></li>
					<li><a href="#contact">CONTACTO</a></li>
					<!-- <li><a href="#mapa">MAPA</a></li> -->
				</ul>
			</div>
		</nav>
		<!-- /Nav -->
		<!-- home wrapper -->
		<div class="home-wrapper">
			<div class="container">
				<div class="row">
					<!-- home content -->
					<div class="col-md-10 col-md-offset-1">
						<div class="home-content">
							<h1 class="white-text">ELECCIONES MASTER</h1>
							<p class="white-text">
								La selección de candidatos es un proceso decisivo para los partidos o movimientos 
								políticos porque puede ayudar al fortalecimiento de la organización.
							</p>
							<br /><br /><br />
							<!-- <button class="white-btn">Get Started!</button>
							<button class="main-btn">Learn more</button> -->
							<a href="login.php" class="bbtn btn-primary btn-lg" role="button"><i class="fa fa-lock"> </i> ACCESO AL PANEL DE CONTROL</a>
						</div>
					</div>
					<!-- /home content -->

				</div>
			</div>
		</div>
		<!-- /home wrapper -->

	</header>
	<!-- /Header -->

	<!-- About -->
	<div id="about" class="section md-padding">

		<!-- Container -->
		<div class="container">

			<!-- Row -->
			<div class="row">

				<!-- Section header -->
				<div class="section-header text-center">
					<h2 class="title">DESCRIPCION DEL SITIO</h2>
				</div>
				<!-- /Section header -->

				<!-- about -->
				<div class="col-md-4">
					<div class="about">
						<i class="fa fa-cogs"></i>
						<h3>Completamente Personalizable</h3>
						<p>El usuario admistrador puede cambiar la configuracion del sistema.</p>
						<a href="#">Leer Más</a>
					</div>
				</div>
				<!-- /about -->

				<!-- about -->
				<div class="col-md-4">
					<div class="about">
						<i class="fa fa-magic"></i>
						<h3>Tipos de Usuarios</h3>
						<p>Varios tipos de usuarios para manipular el sistema.</p>
						<a href="#">Leer Más</a>
					</div>
				</div>
				<!-- /about -->

				<!-- about -->
				<div class="col-md-4">
					<div class="about">
						<i class="fa fa-mobile"></i>
						<h3>Paginas Responsives</h3>
						<p>Se puede manipular el sistema desde cualquier dispositivo ya que la pagina es adaptable</p>
						<a href="#">Leer Más</a>
					</div>
				</div>
				<!-- /about -->

			</div>
			<!-- /Row -->

		</div>
		<!-- /Container -->

	</div>
	<!-- /About -->

	<!-- Service -->
	<div id="service" class="section md-padding">

		<!-- Container -->
		<div class="container">

			<!-- Row -->
			
			<!-- /Row -->

		</div>
		<!-- /Container -->

	</div>
	<!-- /Service -->





	<!-- Numbers -->
	<div id="numbers" class="section sm-padding">

		<!-- Background Image -->
		<div class="bg-img" style="background-image: url('./img/background2.jpg');">
			<div class="overlay"></div>
		</div>
		<!-- /Background Image -->

		<!-- Container -->
		<div class="container">

			<!-- Row -->

			<!-- /Row -->

		</div>
		<!-- /Container -->

	</div>
	<!-- /Numbers -->

	<!-- Pricing -->
	<div id="pricing" class="section md-padding">

		<!-- Container -->

		<div class="container">

			<!-- Row -->
			<div class="row">
				<h3>CANDIDATOS DISPONIBLES</h3>
				<?php do { ?>
				<div class="col-sm-4">
					<div class="pricing">
						<br />
						<p>NOMBRE: <?php echo $row_RstCandidatos['nombre']; ?> <?php echo $row_RstCandidatos['apellido']; ?></p>
						<img src="fotosusuarios/<?php echo $row_RstCandidatos['foto']; ?>" alt="fot"  id="imagen" title="" />
						<br><br>
						Categoría: <?php echo nombrecategoria($row_RstCandidatos['idcategoria']); ?>
						<div class="price-btn">
							<a href="votos_save.php?cedula=<?php echo $row_RstCandidatos['cedula']; ?>" class="btn btn-info btn-lg" role="button"> VOTAR </a>
						</div>
					</div>
				</div>
				<?php } while ($row_RstCandidatos = mysqli_fetch_assoc($RstCandidatos)); ?>
			</div>
			<!-- Row -->

		</div>
		<!-- /Container -->

	</div>
	<!-- /Pricing -->





	<!-- Contact -->
	<div id="contact" class="section md-padding">

		<!-- Container -->
		<div class="container">

			<!-- Row -->
			<div class="row">

				<!-- Section-header -->
				<form class="" role="form"  method="post" action="#" enctype="multipart/form-data" name="FrmDatos">
				<div class="section-header text-center">
					<h2 class="title">CONTACTESE CON NOSOTROS</h2>
				</div>
				<!-- /Section-header -->

				<!-- contact -->
				<div class="col-sm-4">
					<div class="contact">
						<i class="fa fa-phone"></i>
						<h3>TELEFONO</h3>
						<p>3004136691</p>
					</div>
				</div>
				<!-- /contact -->

				<!-- contact -->
				<div class="col-sm-4">
					<div class="contact">
						<i class="fa fa-envelope"></i>
						<h3>Email</h3>
						<p>masijoan@hotmail.com</p>
					</div>
				</div>
				<!-- /contact -->

				<!-- contact -->
				<div class="col-sm-4">
					<div class="contact">
						<i class="fa fa-map-marker"></i>
						<h3>DIRECCION</h3>
						<p>Guarne, barrio Maria Auxilidora</p>
					</div>
				</div>
				<!-- /contact -->

				<!-- contact form -->
				<!-- <div class="col-md-8 col-md-offset-2">
					<form class="contact-form">
						<input type="text" class="input" placeholder="Nombre del Contacto">
						<input type="email" class="input" placeholder="Email">
						<input type="text" class="input" placeholder="Asunto">
						<textarea class="input" placeholder="Mensaje"></textarea>
						<button class="main-btn">Enviar</button>
					</form>
				</div> -->
			</form>
				<!-- /contact form -->

			</div>
			<!-- /Row -->

		</div>
		<!-- /Container -->

	</div>
	<!-- /Contact -->


	<!-- Footer -->
	<footer id="footer" class="sm-padding bg-dark">

		<!-- Container -->
		<div class="container">

			<!-- Row -->
			<div class="row">

				<div class="col-md-12">

					<!-- footer logo -->
					<!-- <div class="footer-logo">
						<a href="index.html"><img src="img/logo-alt.png" alt="logo"></a>
					</div> -->
					<!-- /footer logo -->

					<!-- footer follow -->
					<ul class="footer-follow">
						<li><a href="#"><i class="fa fa-facebook"></i></a></li>
						<li><a href="#"><i class="fa fa-twitter"></i></a></li>
						<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
						<li><a href="#"><i class="fa fa-instagram"></i></a></li>
						<li><a href="#"><i class="fa fa-linkedin"></i></a></li>
						<li><a href="#"><i class="fa fa-youtube"></i></a></li>
					</ul>
					<!-- /footer follow -->

					<!-- footer copyright -->
					<div class="footer-copyright">
						<p>Copyright © 2017. All Rights Reserved. Designed by <a href="#" target="_blank">Colorlib</a></p>
					</div>
					<!-- /footer copyright -->

				</div>

			</div>
			<!-- /Row -->

		</div>
		<!-- /Container -->

	</footer>
	<!-- /Footer -->

	<!-- Back to top -->
	<div id="back-to-top"></div>
	<!-- /Back to top -->

	<!-- Preloader -->
	<div id="preloader">
		<div class="preloader">
			<span></span>
			<span></span>
			<span></span>
			<span></span>
		</div>
	</div>
	<!-- /Preloader -->

	<!-- jQuery Plugins -->
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/owl.carousel.min.js"></script>
	<script type="text/javascript" src="js/jquery.magnific-popup.js"></script>
	<script type="text/javascript" src="js/main.js"></script>

</body>

</html>
