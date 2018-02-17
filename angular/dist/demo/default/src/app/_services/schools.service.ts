import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

@Injectable()
export class SchoolsService {

 constructor(private http: Http) { }

 getSchools() {
   return this.http.get("http://httpbin.org/get").map((response: Response) => response.json());
 }

}
