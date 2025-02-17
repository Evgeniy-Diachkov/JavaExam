# README

Сервис сокращения ссылок предоставляет возможность преобразовывать длинные URL-адреса в короткие ссылки с управлением времени их действия и количеством переходов. Он также позволяет просматривать статистику, редактировать параметры ссылок и управлять ими, предоставляя доступ на редактирование только их владельцам.

## Как пользоваться сервисом

### Запуск
1. Запустите приложение.
2. При первом входе выберите "yes" для регистрации нового пользователя.
3. Введите имя пользователя и получите ваш UUID.
4. При повторном входе выберите "no" и введите имя и UUID для идентификации.

## Команды

| Команда             | Описание                                                                 |
|---------------------|-------------------------------------------------------------------------|
| `create`            | Создать новую короткую ссылку. Введите длинную ссылку, лимит переходов и время жизни. |
| `visit`             | Перейти по короткой ссылке. Браузер откроет оригинальный ресурс.       |
| `stats`             | Показать статистику для указанной короткой ссылки.                    |
| `delete`            | Удалить короткую ссылку. Доступно только владельцу ссылки.            |
| `my_links`          | Просмотреть список всех созданных пользователем ссылок.               |
| `update_clicks`     | Обновить лимит переходов для созданной ссылки. Доступно только владельцу. |
| `exit`              | Выйти из программы.                                                   |

## Как протестировать ваш код

1. **Регистрация и вход:**
   - Зарегистрируйте нового пользователя и убедитесь, что UUID корректно сохраняется.
   - Повторно войдите с введением имени и UUID.

2. **Создание ссылок:**
   - Создайте ссылку с разными параметрами времени жизни и лимита переходов.
   - Убедитесь, что ссылка корректно сохраняется.
   - Убедитесь, что один пользователь может создавать несколько коротких ссылок на разные ресурсы.
   - Убедитесь, что в соответствии с условиями (п.2), указанными в критериях оценивания, срок действия ссылки выбирается минимальным из указанного в конфигурационном файле и заданного пользователем.
   - Убедитесь, что в соответствии с условиями (п.8), указанными в критериях оценивания, предельное количество переходов ссылки выбирается максимальным из указанного в конфигурационном файле и заданного пользователем.

3. **Просмотр статистики:**
   - Используйте команду `stats` для проверки оставшихся переходов и времени жизни ссылки.

4. **Уведомление пользователя:**
   - Убедитесь, что при попытке перехода по «протухшей» ссылке пользователь получает уведомление, когда ссылка становится недоступной из-за лимита переходов ("Лимит переходов исчерпан.") или истечения времени жизни ("Срок действия ссылки истек.").

5. **Удаление и редактирование:**
   - Попробуйте удалить или изменить лимит переходов у ссылки, которую вы создали. Убедитесь, что другим пользователям доступ запрещен.
   - Убедитесь, что реализован функционал автоматического удаления ссылок с истекшим сроком и с 0 оставшихся переходов. Поиск и удаление «протухших» ссылок выполняется при запуске и при завершении программы.

6. **Переход по ссылке:**
   - Введите короткую ссылку с помощью команды `visit`. Убедитесь, что оригинальная ссылка открывается в браузере.

7. **Список ссылок:**
   - Используйте команду `my_links` для просмотра всех ссылок, созданных текущим пользователем.

8. **Работа нескольких пользователей:**
   - Убедитесь, что сервис поддерживает работу нескольких пользователей, каждый из которых может создавать уникальные короткие ссылки.
   - Убедитесь, что разные пользователи получают разные короткие ссылки на один и тот же URL, а при повторном запросе сокращения той же ссылки, пользователю сгенерируется новая ссылка.

9. **Тестирование предельных случаев:**
   - Проверьте ввод некорректных данных, например, неверный UUID или несуществующую короткую ссылку.

## Дополнительно
Для изменения максимального времени жизни ссылки или лимита переходов, заданных по умолчанию, отредактируйте файл `config.json` в корне проекта. 

Сервис использует внешние библиотеки Gson и Lombok. Убедитесь, что они установлены. Для установки библиотек добавьте следующие зависимости в Ваш `pom.xml`


