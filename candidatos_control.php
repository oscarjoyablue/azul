<?php require_once('Connections/cnx.php'); ?>
<?php require_once('Connections/infousers.php'); ?>
<?php include('includes/contadores.php'); ?> 
<?php
    mysqli_select_db($cnx,$database_cnx);
    $query_RstCandidatos = "SELECT * FROM candidatos";
    $RstCandidatos = mysqli_query($cnx,$query_RstCandidatos) or die(mysql_error());
    $row_RstCandidatos = mysqli_fetch_assoc($RstCandidatos);
    $totalRows_RstCandidatos = mysqli_num_rows($RstCandidatos);
    //fin
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
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css"/>    	
    <!-- BEGIN CSS for this page -->
    <style type="text/css">
    #foto {
        height: 50px;
        width: 50px;
        border-radius: 60px;
        border-style: solid;
        border-width: 1px;
        border-color: #f4b906;
        padding: 2px;
    }
    #mdialTamanio
    {
        width: 80% !important;
    }
</style>    
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
                                <h1 class="main-title float-left">Lista de Candidatos </h1>
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
                    <div class="row">
                        <div class="col-xl-12">	
                            <!-- contenido	 -->	
                            <!-- <h3><img src="images/513.jpg" width="50" height="50" alt="" class="img-rounded"/> Datos del candidato </h3>
                            <hr /> -->
                            <?php if($totalRows_RstCandidatos>0){ ?>
                            <table id="tabla" class="table table-striped table table-bordered table-hover table table-sm">
                                <thead>
                                    <tr>
                                        <th>cedula</th>
                                        <th>Nombre</th>
                                        <th>apellido</th>
                                        <th>Categoría</th>
                                        <th>Foto</th>
                                    </tr>
                                </thead>
                                <tbody  bgcolor="#FFFFFF">
                                    <?php do { ?>
                                    <tr>
                                        <td><?php echo $row_RstCandidatos['cedula']; ?></td>
                                        <td><?php echo $row_RstCandidatos['nombre']; ?></td>
                                        <td><?php echo $row_RstCandidatos['apellido']; ?></td>
                                        <td><?php echo nombrecategoria($row_RstCandidatos['idcategoria']); ?></td>
                                        <td><img src="fotosusuarios/<?php echo $row_RstCandidatos['foto']; ?>" alt="fot" name="foto" id="foto" title="Click para ver mas informacion" /></td>
                                    </tr>
                                    <?php } while ($row_RstCandidatos = mysqli_fetch_assoc($RstCandidatos)); ?>
                                </tbody>
                            </table>
                            <?php } else { ?>
                                <div class="alert alert-danger alert-dismissable">
                                <a href="controlpanel_admin.php" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                <H2>TODAVIA NO HAY DATOS EN LA TABLA </H2>
                                <br>
                                <hr>
                                <img src="images/datosvacios.png" width="400" height="100" alt="" id="imagen1"/></div>
                                <hr>
                                <button type="button" class="btn btn-primary" onClick="javascript:history.back()">Salir  <i class="glyphicon glyphicon-log-in"></i>  </button>
                                </div>
                            <?php } ?>
                        </div>
                    </div>
                </div>
                <!-- END container-fluid -->
            </div>
            <!-- END content -->
        </div>
        <!-- END content-page -->
        <?php include('includes/footer_admin.php'); ?>
    </div>
    <!-- END main -->
    <?php include('includes/scripts_admin.php'); ?>
 <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js"></script>  
<script>
    $(document).ready(function() {
        $('#tabla').DataTable( {
            "language": {
                "sProcessing":     "Procesando...",
                "sLengthMenu":     "Mostrar _MENU_ registros",
                "sZeroRecords":    "No se encontraron resultados",
                "sEmptyTable":     "Ningún dato disponible en esta tabla",
                "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
                "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
                "sInfoPostFix":    "",
                "sSearch":         "Buscar:",
                "sUrl":            "",
                "sInfoThousands":  ",",
                "sLoadingRecords": "Cargando...",
                "oPaginate": {
                    "sFirst":    "Primero",
                    "sLast":     "Último",
                    "sNext":     "Siguiente",
                    "sPrevious": "Anterior"
                },
                "oAria": {
                    "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                    "sSortDescending": ": Activar para ordenar la columna de manera descendente"
                }
            },    
            "paging":   true,
            "ordering": false,
            "bJQueryUI": true,
            "iDisplayLength": 10,
            "aLengthMenu": [[5,10, 25, 50, 100, -1], [5,10, 25, 50, 100, "Todo"]],
            "info": true
        } );
    } );
</script> 
</body>
</html>