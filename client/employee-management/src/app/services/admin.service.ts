import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Admin } from '../models/Admin';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl: string = "http://localhost:8080";

  constructor(private http: HttpClient) { }

  signup(admin: Admin): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin`, admin);
  }

  verifyEmail(email:string): Observable<any> {
    return this.http.get(`${this.apiUrl}/verify-email/${email}`);
  }

  signin(email: string, password: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/admin/${email}/${password}`);
  }

}
