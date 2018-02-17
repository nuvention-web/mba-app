import 'dart:async';
import 'dart:io';

main() async {
  var server = await HttpServer.bind(InternetAddress.LOOPBACK_IP_V4, 4049);
  await for (var req in server) {
    ContentType contentType = req.headers.contentType;

    req.response.headers
      ..add('Access-Control-Allow-Origin', '*')
      ..add('Access-Control-Allow-Methods', "GET,POST,PUT,DELETE,OPTIONS")
      ..add('Access-Control-Allow-Headers',
          "Content-Type, Cache-Control, Access-Control-Allow-Headers, Authorization, X-Requested-With");

    if (req.method == 'OPTIONS') {} else {
//      await new Future.delayed(new Duration(seconds: 3));
      req.response
//        ..statusCode = HttpStatus.INTERNAL_SERVER_ERROR

        ..write("DONE.")
        ..close();
    }
    req.response.close();
  }
}
