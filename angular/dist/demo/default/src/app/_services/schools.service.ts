import { Injectable } from '@angular/core';
import { Http, Response,Headers } from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';

let header = new Headers({'Content-Type': 'application/json'});
@Injectable()
export class SchoolsService {

    constructor(private http:Http) {
    }

    getSchools() {
        return this.http.get("https://mba-application.appspot.com/mba/users/john.doe@gmail.com").map((response:Response) => response.json().schools);
    }

    getAllSchools() {
        return this.http.get("https://mba-application.appspot.com/mba" +
            "/schools").map((response:Response) => response.json());
    }

    userDeleteSchool(schoolName:string) {
        this.http.delete("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName);
    }

    userAddSchool(schoolName:string) {
        return this.http.put("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school", {"schools": [schoolName]}
            , {headers: header});
    }

    getSchoolDetails(schoolName:string) {
        return this.http.get("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName).map((response:Response) => response.json());
    }

    getEssay(schoolName:string, essayID:string) {
        return this.http.get("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName + "/essay/" + essayID).map((response:Response) => response.json());
    }

    getAllEssays() {
        return this.http.get("http://mba-application.appspot.com/mba/users/john.doe@gmail.com/essays").map((response:Response) => response.json());
    }

    deleteEssayDraft(schoolName:string, essayID:string, draftID:string) {
        return this.http.delete("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID).map((response:Response) => response.json());
    }

    getNote(schoolName:string, noteID:string) {
        return this.http.get("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName + "/notes/" + noteID).map((response:Response) => response.json());
    }

    getRecommender(schoolName:string, recommenderID:string) {
        return this.http.get("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName + "/recommender/" + recommenderID).map((response:Response) => response.json());
    }

    updateNote(schoolName:string, noteID:string, contents:string, title:string){
        return this.http.put("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName + "/notes/" + noteID, {"contents": contents, "title": title}
            , {headers: header});
    }

    updateRecommender(schoolName:string, recommenderID:string, contents:string){
        return this.http.put("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName + "/recommender/" + recommenderID, {"contents": contents}
            , {headers: header});
    }


    addNote(schoolName:string, contents:string, title:string){
        return this.http.post("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName + "/notes/", {"contents": contents, "title": title}
            , {headers: header});
    }


}