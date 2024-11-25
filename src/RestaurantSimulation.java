import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RestaurantSimulation {

    public static void main(String[] args) {
        System.out.println("Ресторан відкрився. Очікуємо замовлення...");

        // runAsync(): Офіціант приймає замовлення
        CompletableFuture<Void> takeOrderTask = CompletableFuture.runAsync(() -> {
            simulateWork("Офіціант приймає замовлення");
        });

        // supplyAsync(): Кухар готує страву
        CompletableFuture<String> cookingTask = takeOrderTask.thenCompose(aVoid ->
                CompletableFuture.supplyAsync(() -> {
                    simulateWork("Кухар готує основну страву");
                    return "Стейк із овочами";
                }));

        // thenApplyAsync(): Прикраса страви
        CompletableFuture<String> garnishTask = cookingTask.thenApplyAsync(dish -> {
            simulateWork("Кухар додає гарнір і прикрашає страву");
            return dish + " із соусом барбекю та свіжою зеленню";
        });

        // thenAcceptAsync(): Офіціант подає страву клієнту
        CompletableFuture<Void> serveTask = garnishTask.thenAcceptAsync(finalDish -> {
            simulateWork("Офіціант подає страву клієнту");
            System.out.println("Подано замовлення: " + finalDish);
        });

        // thenRunAsync(): Офіціант повідомляє, що замовлення виконано
        CompletableFuture<Void> doneTask = serveTask.thenRunAsync(() -> {
            simulateWork("Офіціант завершує обслуговування");
            System.out.println("Замовлення успішно виконано. Смачного!");
        });

        // Очікуємо завершення всіх задач
        CompletableFuture.allOf(doneTask).join();
        System.out.println("Ресторан закривається. Гарного вечора!");
    }

    // Метод для імітації роботи із затримкою
    private static void simulateWork(String taskName) {
        try {
            System.out.println(taskName + "...");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
