import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { URL, user} from './url-infos'

let header = new Headers({'Content-Type': 'application/json'});

@Injectable()
export class SchoolsService {

    constructor(private http:Http) {
    }

    getSchools() {
        return this.http.get(URL + "/mba/users/" + user, this.jwt()).map((response:Response) => response.json().schools);
    }

    getAllSchools() {
        return this.http.get(URL+"/mba" + "/schools", this.jwt()).map((response:Response) => response.json());
    }

    userDeleteSchool(schoolName:string) {
        this.http.delete(URL+"/mba/users/"+user+"/school/" + schoolName, this.jwt());
    }

    userAddSchool(schoolName:string) {
        return this.http.put(URL+"/mba/users/"+user+"/school", {"schools": [schoolName]}
            , this.jwt(1));
    }

    getSchoolDetails(schoolName:string) {
        return this.http.get(URL+"/mba/users/"+user+"/school/" + schoolName, this.jwt()).map((response:Response) => response.json());
    }

    updateSchoolDetails(schoolName: string, schoolDetail) {
        return this.http.post(URL+"/mba/users/"+user+"/school/" + schoolName, schoolDetail, this.jwt())
    }

    getEssay(schoolName:string, essayID:string) {
        return this.http.get(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID, this.jwt()).map((response:Response) => response.json());
    }

    getEssayReviews(schoolName:string, essayID:string) {
        return this.http.get(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID + "/reviews", this.jwt()).map((response:Response) => response.json());
    }

    getDraft(schoolName:string, essayID:string, draftID:string) {
        return this.http.get(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID, this.jwt()).map((response:Response) => response.json());
    }

    runAnalysis(schoolName:string, essayID:string, draftID:string){
        return this.http.post(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID + "/scan/analysis",{}, this.jwt(1));
    }

    runProofRead(schoolName:string, essayID:string, draftID:string){
        return this.http.post(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID + "/scan/proofRead",{}, this.jwt(1));
    }

    getAllEssays() {
        return this.http.get(URL+"/mba/users/"+user+"/essays", this.jwt()).map((response:Response) => response.json());
    }

    deleteEssayDraft(schoolName:string, essayID:string, draftID:string) {
        return this.http.delete(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID, this.jwt());
    }

    downloadEssayDraft(schoolName:string, essayID:string, draftID:string) {
        window.open(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID);
    }

    getNote(schoolName:string, noteID:string) {
        return this.http.get(URL+"/mba/users/"+user+"/school/" + schoolName + "/notes/" + noteID, this.jwt()).map((response:Response) => response.json());
    }

    getRecommender(schoolName:string, recommenderID:string) {
        return this.http.get(URL+"/mba/users/"+user+"/school/" + schoolName + "/recommender/" + recommenderID, this.jwt()).map((response:Response) => response.json());
    }

    updateNote(schoolName:string, noteID:string, contents:string, title:string){
        return this.http.put(URL+"/mba/users/"+user+"/school/" + schoolName + "/notes/" + noteID, {"contents": contents, "title": title}
            , this.jwt(1));
    }

    updateRecommender(schoolName:string, recommenderID:string, contents:string){
        return this.http.put(URL+"/mba/users/"+user+"/school/" + schoolName + "/recommender/" + recommenderID, {"contents": contents}
            , this.jwt(1));
    }


    addNote(schoolName:string, contents:string, title:string){
        return this.http.post(URL+"/mba/users/"+user+"/school/" + schoolName + "/notes/", {"contents": contents, "title": title}
            , this.jwt(1));
    }

    uploadEssayDraft(file, schoolName:string, essayID:string) {
        let formData: FormData = new FormData();
        formData.append('file', file, file.name);
        return this.http.post(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID+"/upload/draft", formData, this.jwt(0));
    }

    saveEssayDraft(text, schoolName, essayID) {
        return this.http.post(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID + "/draft",{"contents": text}, this.jwt(1));
    }

    sendForReview(schoolName, essayID, draftID, email, name, message) {
        return this.http.post(URL+"/mba/users/"+user+"/school/" + schoolName + "/essay/" + essayID+"/draft/"+draftID+"/email/", {
            "email": email,
            "name": name,
            "message": message
        }, this.jwt(1));
    }

    private jwt(json=0) {
        // create authorization header with jwt token
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.AUTH_TOKEN) {
            let headers = new Headers;
            if(json==1) {
                headers = new Headers({'Authorization': currentUser.AUTH_TOKEN, 'Content-Type': 'application/json'});
            } else {
                headers = new Headers({ 'Authorization': currentUser.AUTH_TOKEN});
            }
            return new RequestOptions({ headers: headers });
        }
    }


}