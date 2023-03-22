import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { License } from '../DTOS/License';
import { Product } from '../DTOS/Product';
import { ProductDTO } from '../DTOS/ProductDTO';

const baseUrl = "http://localhost:9000/productDetails";
const baseUrlProduct = "http://localhost:9000/product";
@Injectable({
  providedIn: 'root'
})
export class ProductService {

  public getAllLicense():Observable<License[]>{
	  return this.httpClient.get<License[]>(baseUrl+'/getLicense');
  }

  getProductByIdAndVersion(productId: number, version: string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/getByIdAndVersion', null, {
      params: {
        productId: productId,
        version: version
      }
    });
  }


  createNewVersion(product: Product, newVersion: string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/createNewVersionV2', product, {
      params: {
        newVersion: newVersion
      }
    }
    )
  }

  deleteProduct(product: Product): Observable<boolean> {
    return this.httpClient.delete<boolean>(baseUrlProduct + "/deleteProduct/" + product.id);
  }
  createNewProduct(sellerid: number): Observable<ProductDTO> {
    return this.httpClient.post<ProductDTO>(baseUrlProduct+"/createNewProduct", null);
  }

  constructor(private httpClient: HttpClient) { }

  public getProductById(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(baseUrl + "/getActiveVersion", {
      params: {
        productId: productId
      }
    });
  }

  public getProductsCountBySellerId(sellerId: number): Observable<number> {
    return this.httpClient.get<number>("http://localhost:9000/product" + "/getProductsCountBySellerId", {
      params: {
        sellerId: sellerId
      }
    });
  }
  public GetProductDetails(sellerId: number): Observable<Product[]>{
    return this.httpClient.get<Product[]>(baseUrl+"/getProductDetails",{
      params:{
        sellerId: sellerId
      }
    })
  }

  getAllProducts(): Observable<any>{
    return this.httpClient.get<any>(baseUrl + '/getAllProducts');
  }

  getProductsByKeyword(keyword: string): Observable<any>{
    return this.httpClient.get<any>(baseUrl + "/getProductsByKeyword/" + keyword);
  }

  getFilteredProducts(keyword: string, categoryid: number, min: number, max: number): Observable<any>{
    const params = {
      keyword: keyword,
      categoryid: categoryid,
      min: min,
      max: max
    }
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getFilteredProducts", {params});
  }

  getProductsBySellerForSeller(sellerid: number, keyword: string,categoryid: number, min: number, max: number): Observable<any>{
    const params = {
      sellerid: sellerid,
      keyword: keyword,
      categoryid: categoryid,
      min: min,
      max: max
    }
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getProductsBySellerForSeller", {params});
  }

  getProductsBySellerForUser(sellerid: number, keyword: string,categoryid: number, min: number, max: number): Observable<any>{
    const params = {
      sellerid: sellerid,
      keyword: keyword,
      categoryid: categoryid,
      min: min,
      max: max
    }
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getProductsBySellerForUser", {params});
  }

  getProductsByReportStatus(status: string): Observable<any>{
    const params = {
      reportStatus: status
    }
    return this.httpClient.get<any>("http://localhost:9000/product/getProductsByReportStatus", {params})
  }

  getByApprovalStatus(status: string): Observable<any>{
    const params = {
      status: status
    }
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getByApprovalStatus",{params})
  }

  updateApprovalStatus(productId: number, version: string, status: string): Observable<any>{
    const params = {
      productId: productId,
      version: version,
      status: status
    }
    return this.httpClient.put<any>("http://localhost:9000/productDetails/updateApprovalStatus",null,{params});
  }

  getByIdAndVersion(productId: number, version: string): Observable<any>{
    const params = {
      productId: productId,
      version: version
    }
    return this.httpClient.post<any>("http://localhost:9000/productDetails/getByIdAndVersion",null,{params});
  }

  getAllProductsLatestVers(){
    return this.httpClient.get<any>("http://localhost:9000/productDetails/allProductsLatestVers");
  }

  getActiveVersion(productId: number){
    const params = {
      productId: productId
    }
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getActiveVersion",{params});
  }
}
