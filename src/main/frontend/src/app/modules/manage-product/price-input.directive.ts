import { Directive, HostListener, ElementRef, Renderer2, Input } from '@angular/core';

@Directive({
  selector: '[price-input]'
})
export class PriceInputDirective {
  private regex: RegExp = new RegExp(/^(\d{1,3}([ .]\d{3})*|\d+)(\.\d{0,2})?$/g);
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home', '-', '.', 'ArrowLeft', 'ArrowRight'];

  @Input() appPriceInput: string = 'vi-VN';

  constructor(private el: ElementRef) { }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    if (this.specialKeys.indexOf(event.key) !== -1) {
      return;
    }
    const current: string = this.el.nativeElement.value;
    const next: string = current.concat(event.key);
    if (next && !String(next).match(this.regex)) {
      event.preventDefault();
    }
  }

  @HostListener('input', ['$event'])
  onInputChange(event: any) {
    const initialValue = this.el.nativeElement.value;
    const value = this.formatValue(event.target.value);
    if (initialValue !== value) {
      this.el.nativeElement.value = value;
    }
  }

  private formatValue(value: number): string {
    const formatter = new Intl.NumberFormat(this.appPriceInput, {
      style: 'currency',
      currency: 'VND',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    });
    const formattedValue = formatter.format(value).replace(/\D/g, '');
    const integerPart = formattedValue.slice(0, -3);
    const decimalPart = formattedValue.slice(-3);
    const result = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '.' + decimalPart;
    return result;
  }
}
