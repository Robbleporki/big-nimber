import java.util.Scanner;

public class CalculatorInt {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== ماشین حساب ساده جاوا ===");
        System.out.println("عملیات‌های موجود:");
        System.out.println("1. جمع (+)");
        System.out.println("2. تفریق (-)");
        System.out.println("3. ضرب (*)");
        System.out.println("4. تقسیم (/)");
        System.out.println("5. باقیمانده (%)");
        
        // دریافت دو عدد از کاربر
        System.out.print("\nعدد اول را وارد کنید: ");
        int num1 = scanner.nextInt();
        
        System.out.print("عدد دوم را وارد کنید: ");
        int num2 = scanner.nextInt();
        
        // نمایش منو و دریافت عملگر
        System.out.print("\nعملیات مورد نظر را انتخاب کنید (1-5): ");
        int operation = scanner.nextInt();
        
        // انجام عملیات
        switch (operation) {
            case 1:
                System.out.println("\nنتیجه: " + num1 + " + " + num2 + " = " + (num1 + num2));
                break;
            case 2:
                System.out.println("\nنتیجه: " + num1 + " - " + num2 + " = " + (num1 - num2));
                break;
            case 3:
                System.out.println("\nنتیجه: " + num1 + " * " + num2 + " = " + (num1 * num2));
                break;
            case 4:
                if (num2 != 0) {
                    System.out.println("\nنتیجه: " + num1 + " / " + num2 + " = " + ((double) num1 / num2));
                } else {
                    System.out.println("\nخطا: تقسیم بر صفر امکان‌پذیر نیست!");
                }
                break;
            case 5:
                if (num2 != 0) {
                    System.out.println("\nنتیجه: " + num1 + " % " + num2 + " = " + (num1 % num2));
                } else {
                    System.out.println("\nخطا: محاسبه باقیمانده بر صفر امکان‌پذیر نیست!");
                }
                break;
            default:
                System.out.println("\nخطا: عملیات نامعتبر!");
        }
        
        scanner.close();
    }
}