<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Elecciones Master 2021</title>
        <meta name="description" content="Love Authority." />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" />
        <link rel="stylesheet" href="css/stylelogin.css" />
        <!-- <link href="assets/plugins/select2/css/select2.min.css" rel="stylesheet" type="text/css"/> -->
        <style type="text/css">
        /*select {
        border: 1px solid #ccc;
        width: 140px;
        overflow: hidden;
        }*/
        #select{
        padding: 5px 8px;
        width: 100%;
        border: none;
        box-shadow: none;
        background-color: #ff5722 !important;
        background-image: none;
        appearance: none;
  }
        </style>
    </head>
    <body>
        <!--hero section-->
        <section class="hero">
            <div class="container">
                <div class="row">
                    <div class="col-md-6 col-sm-8 mx-auto">
                        <div class="card border-none">
                            <div class="card-body">
                                <div class="mt-2">
                                    <img src="fotosusuarios/avatar.png" alt="Male" class="brand-logo mx-auto d-block img-fluid rounded-circle"/>
                                </div>
                                <p class="mt-4 text-white lead text-center">
                                    Inicie sesión para acceder a su cuenta de Autorizacion
                                </p>
                                <div class="mt-4">
                                    <form action="accesosistema.php" method="POST" class="margin-bottom-0" name="form1" id="form1">
                                        <div class="form-group">
                                            <span>Usuario</span>  
                                            <input type="text" class="form-control" id="Usuario" name="Usuario" placeholder="Nombre de Usuario" autocomplete="off">
                                        </div>
                                        <div class="form-group">
                                            <span>Contraseña</span>  
                                            <input type="password" class="form-control" id="password" name="Password" placeholder="Entre la contraseña" autocomplete="off">
                                        </div>      
                                        <button type="submit" class="btn btn-primary float-right">ENTRAR</button>
                                    </form>
                                    <a class="pull-right text-muted" href="#" id="olvidado" style="font-size:12px;font-weight:bold;background-color:#ffffff;margin-right:2px;padding:4px;border-radius: 20px">Olvidó la contraseña?</a>
                                    <div class="clearfix"></div>
                                    <p class="content-divider center mt-4"><span>or</span></p>
                                </div>
                                <p class="mt-4 social-login text-center">
                                    <a href="#" class="btn btn-twitter"><em class="ion-social-twitter"></em></a>
                                    <a href="#" class="btn btn-facebook"><em class="ion-social-facebook"></em></a>
                                    <a href="#" class="btn btn-linkedin"><em class="ion-social-linkedin"></em></a>
                                    <a href="#" class="btn btn-google"><em class="ion-social-googleplus"></em></a>
                                    <a href="#" class="btn btn-github"><em class="ion-social-github"></em></a>
                                </p>
                                <!-- <p class="text-center">
                                    Don't have an account yet? <a href="register.html">Sign Up Now</a>
                                </p> -->
                            </div>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                    <div class="col-sm-12 mt-5 footer">
                        <!-- <p class="text-white small text-center">
                            &copy; 2017 Login/Register Forms. A FREE Bootstrap 4 component by 
                            <a href="https://wireddots.com">Wired Dots</a>. Designed by <a href="https://twitter.com/attacomsian">@attacomsian</a>
                        </p> -->
                    </div>
                </div>
            </div>
        </section>
<script src="assets/js/modernizr.min.js"></script>
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/moment.min.js"></script>
<script src="assets/js/popper.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
<script src="assets/js/detect.js"></script>
<script src="assets/js/fastclick.js"></script>
<script src="assets/js/jquery.blockUI.js"></script>
<script src="assets/js/jquery.nicescroll.js"></script>
<script src="assets/js/jquery.scrollTo.min.js"></script>
<script src="assets/plugins/switchery/switchery.min.js"></script>
<!-- App js -->
<script src="assets/js/pikeadmin.js"></script>
<script src="assets/plugins/select2/js/select2.min.js"></script>
<!-- <script>                                
$(document).ready(function() {
    $('.select2').select2();
});
</script> -->
    </body>
</html>
