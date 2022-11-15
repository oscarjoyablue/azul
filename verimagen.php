<?php require_once('Connections/cnx.php'); ?>
<?php require_once('Connections/infousers.php'); ?>
<?php
    $Idproducto=$_GET['idproducto'];
    mysqli_select_db($cnx,$database_cnx);
    $query_RstProductos = "SELECT * FROM productos WHERE idproducto=$Idproducto";
    $RstProductos = mysqli_query($cnx,$query_RstProductos) or die(mysql_error());
    $row_RstProductos = mysqli_fetch_assoc($RstProductos);
    $totalRows_RstProductos = mysqli_num_rows($RstProductos);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Elecciones Master 2021</title>
    <meta name="description" content="Free Bootstrap 4 Admin Theme | Pike Admin">
    <meta name="author" content="Pike Web Development - https://www.pikephp.com">
    <!-- Favicon -->
    <link rel="shortcut icon" href="assets/images/favicon.ico">
    <!-- Switchery css -->
    <link href="assets/plugins/switchery/switchery.min.css" rel="stylesheet" />
    <!-- Bootstrap CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <!-- Font Awesome CSS -->
    <link href="assets/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Custom CSS -->
    <link href="assets/css/style.css" rel="stylesheet" type="text/css" />	
    <!-- BEGIN CSS for this page -->
    <!-- END CSS for this page -->
</head>
<body class="adminbody">
    <div id="main">
        <!-- top bar navigation -->
        <?php include('includes/headerbar_admin.php'); ?>
        <!-- End Navigation -->
        <!-- Left Sidebar -->
        <?php include('includes/leftmainsidebar_admin.php'); ?>
        <!-- End Sidebar -->
        <div class="content-page">
            <!-- Start content -->
            <div class="content">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-xl-12">
                            <div class="breadcrumb-holder">
                                <h1 class="main-title float-left">Titulo de la pagina</h1>
                                <ol class="breadcrumb float-right">
                                    <li class="breadcrumb-item">Inicio</li>
                                    <li class="breadcrumb-item active">Pagina Actual</li>
                                </ol>
                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
                    <!-- end row -->
                    <!-- CONTENIDO DE LA PAGINA-->
                    <!-- <form class="" role="form"  method="post" action="entradas_add.php?idproducto=<?php echo $row_RstProductos['idproducto']; ?>"  name="FrmDatos"> -->
                    <form class="" role="form"  method="post" action="entradasalida_add.php?idproducto=<?php echo $row_RstProductos['idproducto']; ?>"  name="FrmDatos">    
                        <div class="card mb-5" >
                            <div class="card-header">
                                <h3><i class="fa fa-credit-card"></i> NOMBRE DEL PRODUCTO</h3>
                                <div class="alert alert-success" role="alert">
                                <?php echo $row_RstProductos['nombre']; ?>
                                </div>
                            </div>
                            <div class="card-body" >
                                <!-- <button type="submit" class="btn btn-success"> <i class="fa fa-save"></i> Cambiar Imagen </button> -->
                                <a href="imagen_edit.php?idproducto=<?php echo $row_RstProductos['idproducto']; ?>" class="btn btn-outline-danger" role="button"><i class="fa fa-file-photo-o"> </i> Cambiar Imagen </a>
                                <br />
                                <div class="card" style="width: 25rem;">
                                    <img class="card-img-top" src="images/<?php echo $row_RstProductos['imagen']; ?>" alt="Card image cap">
                                    <div class="card-body">

                                        <h4 class="card-title">INFO DEL PRODUCTO </h4>
                                        <p class="card-text">PRECIO : <?php echo ' $'.number_format($row_RstProductos['precio'], 2, ',', '.'); ?></p>
                                        <p class="card-text">CANTIDAD : <?php echo $row_RstProductos['cantidad']; ?></p>
                                        <hr />
                                        <button type="submit" name="ok" value="Comprar" class="btn btn-success"> <i class="fa fa-dollar"></i> Comprar </button>
                                        <button type="submit" name="ok" value="Vender" class="btn btn-warning"> <i class="fa fa-dollar"></i> Vender </button>    
                                        <button type="button" class="btn btn-primary" onClick="javascript:history.back()">Salir</button>
                                    </div>
                                </div>
                            </div>                                                      
                        </div><!-- end card-->  
                    </form>
                    <!-- END container-fluid -->
                </div>
                <!-- END content -->
            </div>
            <!-- END content-page -->
            <?php include('includes/footer_admin.php'); ?>
        </div>
        <!-- END main -->
        <?php include('includes/scripts_admin.php'); ?>
    </body>
    </html>