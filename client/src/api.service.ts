import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse  } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { of } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) {
   }

  public getBooks(term:string): Observable<any>{
    if (term === '') {
      return of([]);
    }
    return this.http.get('https://www.googleapis.com/books/v1/volumes?q=' + term + '&maxResults=40');
  }
  //done
  public getUser(username: string): Observable<any>{
    return this.http.get(`http://localhost:8080/api/get-user?username=${username}`)
  }

  //done
  public signUp(form: any): Observable<any>{
    return this.http.post('http://localhost:8080/api/signup', form)
  }

  //done
  public editProfile(user: string, userId: number){
    return this.http.put(`http://localhost:8080/api/update-profile?userId=${userId}`, user)
  }
  //done
  public addBookToCart(userId: number,  book: string): Observable<any>{
    return this.http.post(`http://localhost:8080/api/cart/add-cart?userId=${userId}`, book, {responseType: 'text'});
  }
  //done
  public getBooksForCart(userId: number):Observable<any>{
    return this.http.get(`http://localhost:8080/api/cart/get-cart?userId=${userId}`)
  }
  //done
  public deleteFromCart(isbn: number, userId: number){
    return this.http.delete(`http://localhost:8080/api/cart/delete-from-cart?isbn=${isbn}&userId=${userId}`)
  }
  //done
  public deleteAllFromCart(userId: number){
    return this.http.delete(`http://localhost:8080/api/cart/delete-all-from-cart?userId=${userId}`)
  }
  //done
  public addBookAllReserved(userId: number, book: string){
    return this.http.post(`http://localhost:8080/api/reserved/add-all-reserved?userId=${userId}`, book);
  }
  //done
  public getBooksForReserved(userId: number){
    return this.http.get(`http://localhost:8080/api/reserved/get-reserved?userId=${userId}`)
  }
  //done
  public deleteFromReserved(isbn: number, userId: number){
    return this.http.delete(`http://localhost:8080/api/reserved/delete-from-reserved?isbn=${isbn}&userId=${userId}`)
  }
  //done
  public deleteAllFromReserved(userId: number){
    return this.http.delete(`http://localhost:8080/api/reserved/delete-all-from-reserved?userId=${userId}`)
  }
  //done
  public addBookToWishlist(userId: number, book: string){
    return this.http.post(`http://localhost:8080/api/wishlist/add-wish-list?userId=${userId}`, book, {responseType: 'text'});
  }
  //done
  public getBooksForWishlist(userId: number){
    return this.http.get(`http://localhost:8080/api/wishlist/get-wishlist?userId=${userId}`)
  }
  //done
  public deleteFromWishlist(isbn: number, userId: number){
    return this.http.delete(`http://localhost:8080/api/wishlist/delete-from-wishlist?isbn=${isbn}&userId=${userId}`)
  }
  //done
  public deleteAllFromWishlist(userId: number){
    return this.http.delete(`http://localhost:8080/api/wishlist/delete-all-from-wishlist?userId=${userId}`)
  }


}
