<!DOCTYPE html>
<html lang="en">

  <head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Social Sharing -->
    <meta name="description" content="Considering an MBA? We've got your back. All for free.">
    <meta name="author" content="myapp.MBA">
    <meta property="og:url" content="https://myapp.mba" />
    <meta property="og:type" content="website" />
    <meta property="og:title" content="myapp.MBA" />
    <meta property="og:description" content="Considering an MBA? We've got your back. All for free." />
    <meta property="og:image" content="https://myapp.mba/img/fb-share.png" />
    <link rel="shortcut icon" href="img/favicon.png" />

    <title>myapp.MBA</title>

    <!-- Custom fonts for this template -->
    <link rel="stylesheet" href="https://use.typekit.net/jiy6xnx.css">
    <link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- Custom styles for this template -->
    <link href="css/landing-page.min.css" rel="stylesheet">
    <!-- Facebook Pixel Code -->
    <script>
      !function(f,b,e,v,n,t,s)
      {if(f.fbq)return;n=f.fbq=function(){n.callMethod?
      n.callMethod.apply(n,arguments):n.queue.push(arguments)};
      if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';
      n.queue=[];t=b.createElement(e);t.async=!0;
      t.src=v;s=b.getElementsByTagName(e)[0];
      s.parentNode.insertBefore(t,s)}(window, document,'script',
      'https://connect.facebook.net/en_US/fbevents.js');
      fbq('init', '162048144617616');
      fbq('track', 'PageView');
    </script>
    <noscript><img height="1" width="1" style="display:none"
      src="https://www.facebook.com/tr?id=162048144617616&ev=PageView&noscript=1"
    /></noscript>
    <!-- End Facebook Pixel Code -->
  </head>

  <body>

    <!-- Navigation -->
    <nav class="navbar navbar-dark bg-dark static-top">
      <div class="container">
        <a class="navbar-brand" href="#"><img src="img/logo_default_dark.png"></img></a>
        <a class="btn btn-primary" href="mailto:hello@myapp.mba">Get in Touch</a>
      </div>
    </nav>

    <!-- Masthead -->
    <header id="masthead" class="masthead text-white text-center">
      <div class="overlay"></div>
      <div class="container">
        <div class="row">
          <div class="col-md-6">
          <div id="action-box" class="card">
            <div class="card-body">
              <div class="row">
                <div class="col-xl-9 mx-auto">
                  <h2 class="mb-3">Considering an MBA?<br><small>Focus on your story, we'll take care of the rest.</small></h1>
                </div>
                <div class="col-md-11 mx-auto">
                  <form id="signup">
                    <div class="form-row">
                      <div class="col-12">
                        <div id="error" class="hidden alert alert-danger" role="alert">

                        </div>
                      </div>
                    </div>
                    <div class="form-row" style="margin-bottom: 10px;">
                      <div class="col-12">
                        <input id="email" name="email" autocomplete="email" type="email" class="form-control form-control-lg" placeholder="Enter your email...">
                      </div>
                    </div>
                    <div id="form-hidden" class="hidden">
                      <div class="form-row">
                        <div class="col-6">
                          <div class="form-group">
                            <label for="fname">First Name *</label>
                            <input id="fname" autocomplete="given-name" type="text" class="form-control form-control-lg" placeholder="First Name">
                          </div>
                        </div>
                        <div class="col-6">
                          <div class="form-group">
                            <label for="lname">Last Name *</label>
                            <input id="lname" autocomplete="family-name" type="text" class="form-control form-control-lg" placeholder="Last Name">
                          </div>
                        </div>
                      </div>
                      <div class="form-row">
                        <div class="col-6">
                          <div class="form-group">
                            <label for="dob">Date of Birth *</label>
                            <input id="dob" type="date" autocomplete="bday" class="form-control form-control-lg">
                          </div>
                        </div>
                        <div class="col-6">
                          <div class="form-group">
                            <label for="occ">Occupation *</label>
                            <input id="occ" type="text" class="form-control form-control-lg" placeholder="Occupation">
                          </div>
                        </div>
                      </div>
                    </div>
                    <input id="utm_source" type="hidden" value="<?=$_GET['utm_source']?>">
                    <input id="utm_medium" type="hidden" value="<?=$_GET['utm_medium']?>">
                    <input id="utm_campaign" type="hidden" value="<?=$_GET['utm_campaign']?>">
                    <input id="utm_term" type="hidden" value="<?=$_GET['utm_term']?>">
                    <input id="utm_content" type="hidden" value="<?=$_GET['utm_content']?>">
                    <div class="form-row">
                      <div class="col-12">
                        <button id="submit" type="submit" class="btn btn-block btn-lg btn-dark">Reserve Your Invite</button>
                      </div>
                    </div>
                  </form>
                  <div id="success" class="hidden alert alert-success" role="alert">
                    Thank you for adding your name. We will be in touch shortly.
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      </div>
    </header>

    <!-- Icons Grid -->
    <section class="features-icons bg-dark text-center">
      <div class="container">
        <div class="row">
          <div class="col-lg-4">
            <div class="features-icons-item mx-auto mb-5 mb-lg-0 mb-lg-3">
              <div class="features-icons-icon d-flex">
                <i class="fa fa-edit m-auto"></i>
              </div>
              <h3>Essay Drafts, Wrangled</h3>
              <p class="lead mb-0">Write, manage, and get feedback on your essays all in one place.</p>
            </div>
          </div>
          <div class="col-lg-4">
            <div class="features-icons-item mx-auto mb-5 mb-lg-0 mb-lg-3">
              <div class="features-icons-icon d-flex">
                <i class="fa fa-calendar m-auto"></i>
              </div>
              <h3>Deadlines, Managed</h3>
              <p class="lead mb-0">Keep track of all your application deadlines in one place</p>
            </div>
          </div>
          <div class="col-lg-4">
            <div class="features-icons-item mx-auto mb-0 mb-lg-3">
              <div class="features-icons-icon d-flex">
                <i class="fa fa-comments m-auto"></i>
              </div>
              <h3>Advice, On-Demand</h3>
              <p class="lead mb-0">Specialists will review you application for a small fee.</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Image Showcases -->
    <section class="showcase">
      <div class="container-fluid p-0">
        <div class="row no-gutters">

          <div class="col-lg-6 order-lg-2 text-white showcase-img" style="background-image: url('img/bg-showcase-1.jpg');"></div>
          <div class="col-lg-6 order-lg-1 my-auto showcase-text">
            <h2>Essay Drafts, Wrangled</h2>
            <p class="lead mb-0">Writing your application essays can be the hardest part of the application process. myapp.MBA allows you to focus on your story—we take care of the rest with a suite of tools to make managing drafts, versions, and prompts a breeze.</p>
          </div>
        </div>
        <div class="row  no-gutters">
          <div class="col-lg-6 text-white showcase-img" style="background-image: url('img/bg-showcase-2.jpg');"></div>
          <div class="col-lg-6 my-auto showcase-text">
            <h2>Deadlines, Managed</h2>
            <p class="lead mb-0">Every school has different requirements and timelines. Managing your deadlines doesn't have to be a pain. myapp.MBA keeps track of it all so you can focus on the more important things.</p>
          </div>
        </div>
        <div class="row  no-gutters">
          <div class="col-lg-6 order-lg-2 text-white showcase-img" style="background-image: url('img/bg-showcase-3.jpg');"></div>
          <div class="col-lg-6 order-lg-1 my-auto showcase-text">
            <h2>Advice, On-Demand</h2>
            <p class="lead mb-0">If you get stuck on an essay or have a question, we have specialists who can review your application materials for a fraction of what an MBA consultant would cost.</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Call to Action -->
    <section class="call-to-action text-white text-center">
      <div class="overlay"></div>
      <div class="container">
        <div class="row">
          <div class="col-xl-9 mx-auto">
            <h2 class="mb-4">Want the invite when we launch?* Sign up now.</h2>
          </div>
          <div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
              <a href="#masthead" class="btn btn-block btn-lg btn-primary">Get an Invite</a>
          </div>
          <div class="col-xl-9 mx-auto">
            <br>
            <p>* myapp.MBA will launch in a private beta with a limited number of invitations. Sign up to save your spot.</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Footer -->
    <footer class="footer bg-light">
      <div class="container">
        <div class="row">
          <div class="col-lg-6 h-100 text-center text-lg-left my-auto">
            <ul class="list-inline mb-2">
              <li class="list-inline-item">
                <a href="mailto:hello@myapp.mba">Contact</a>
              </li>
              <li class="list-inline-item">&sdot;</li>
              <li class="list-inline-item">
                <a href="" data-toggle="modal" data-target="#privacy-modal">Privacy Policy</a>
              </li>

            </ul>
            <p class="text-muted small mb-4 mb-lg-0">&copy; myapp.MBA 2018. All Rights Reserved.</p>
          </div>
          <div class="col-lg-6 h-100 text-center text-lg-right my-auto">
            Are you an MBA consultant, test prep company, or advertiser interested in partnering? <a href="mailto:hello@myapp.mba">Contact us</a>!
          </div>
        </div>
      </div>
    </footer>

    <div id="privacy-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Privacy Policy</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <p>When you use myapp.MBA’s website, we collect the personally identifiable information from you that you voluntarily share with us such as your email address, name, and occupation.</p>

            <p>We also collect non-identifiable information about you using cookies, Google Analytics, and Facebook pixels.</p>

            <p>We may use this information to improve our product and offer you targeted promotions. We will not sell your personally identifiable information to third parties.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Bootstrap core JavaScript -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <script src="js/script.js"></script>

    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-117478607-1"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());
      gtag('config', 'UA-117478607-1');
    </script>


  </body>

</html>
