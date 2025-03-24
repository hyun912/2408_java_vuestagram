package com.example.vuestagram.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.Arrays;

public class OrderSpecifierUtil {
	// 동적 정렬 정보 생성 메서드
	public static <T> OrderSpecifier<?>[] generateOrderSpecifier(Pageable pageable, EntityPathBase<T> qClass) {
		// QueryDSL의 `orderBy()`의 경우 아래처럼 오버로딩 되어 있다.
		//      - public Q orderBy(OrderSpecifier<?> o) {} : OrderSpecifier<?>객체 하나만 받는 메소드
		//      - public Q orderBy(OrderSpecifier<?>... o) {} : OrderSpecifier<?>[] 배열을 받는 메소드

		// `OrderSpecifierUtil.generateOrderSpecifier()`의 경우 정렬 기준이 복수로도 오는걸 전제로 하여,
		// 최종적으로 OrderSpecifier<?>[] 배열을 리턴하도록 작성되어 있음.
		// 이때, 배열의 순서가 정렬의 우선순위를 가짐.

		// 예를 들어, 아래와 같이 요청이 왔다면,
		//      http://localhost:8080/api/boards/search?page=1&size=5&sort=likes,ASC&sort=createdAt,DESC
		// 리턴 OrderSpecifier<?>[] 배열은 다음과 같다.
		//      [
		//          likes가 ASC인 OrderSpecifier 객체 (최종적으로 `QClass.likes.acs()`)
		//          ,createdAt가 DESC인 OrderSpecifier 객체 (최종적으로 `QClass.createdAt.DESC()`)
		//      ]
		// 그러면 `OrderSpecifierUtil.generateOrderSpecifier()`를 호출한 곳에서는 아래와 같은 형태가 됨
		//      query.orderBy(new OrderSpecifier[]{board.createdAt.asc(), board.boardId.asc()})

		// Pageable에서 Sort를 스트림으로 만들어, 새로운 OrderSpecifier<?>[] 배열을 만들기위해 map로 처리
		return pageable.getSort().stream().map(order -> {
			// 프로퍼티 존재 여부 체크
			if(!OrderSpecifierUtil.isPropertyExist(qClass, order.getProperty())) {
				throw new IllegalArgumentException("정렬 기준값 이상");
			}

			Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC; // 정렬의 방향을 획득

			// 새로운 OrderSpecifier<?> 객체를 인스턴스
			// 첫번째 파라미터(Order order) : 정렬의 방향정보를 가진 Order 객체, 32라인에서 획득
			// 두번째 파라미터(Expressions<T>) : QueryDSL에서 사용할 Path, 예)`QBoard.userId`, `QBoard.createAt` 등 사용
			//      두번째 파라미터(Expressions<T>)의 경우 클라이언트가 보내온 데이터로 작성해야 하므로 동적으로 Path를 생성해줄 필요가 있음
			//      `Expressions.path(Class<? extends T> type, Path<?> parent, String property)`의 파라미터는 아래와 같음
			//          - 첫번째 파라미터(Class<? extends T> type)
			//              > 정렬 필드의 타입 지정
			//              > 필드의 타입에 따라 에 따라 String.class, LocalDateTime.class, Long.class 등을 사용
			//              > 현 처리에서는 동적 처리이므로 정렬 필드 타입을 명확히 알 수 없으므로 최상위 객체인 `Object.class`를 사용
			//          - 두번째 파라미터(Path<?> parent)
			//              > 동적으로 주어지는 QClass, 예) `QUser`, `QBoard` 등
			//          - 세번째 파라미터(String property)
			//              > 정렬 기준이되는 속성명, 예) "createdAt", "content" 등
			//      예) `qClass`가 `QBoard`이고, `order.getProperty()`가 `createdAt`인 경우,
			//          `Expressions.path(Object.class, qClass, order.getProperty())`의 결과는 `QBoard.createdAt`의 형태가 됨
			return new OrderSpecifier(direction, Expressions.path(Object.class, qClass, order.getProperty()));
		})
		.toArray(OrderSpecifier[]::new); // 리스트를 OrderSpecifier[] 배열로 만들어 반환
		// `Stream.toArray(OrderSpecifier[]::new)`의 동작 원리는 아래와 같음
		//      - `Stream.toArray(IntFunction<T[]>)`의 경우 내부적으로 전달된 배열을 기반으로 필요한 크기의 배열을 직접 생성
		//      - `OrderSpecifier[]::new`는 람다 표현식 방식의 배열 생성자 참조로 `size -> new OrderSpecifier[size]` 와 같음
		//      - 따라서 Stream.toArray(OrderSpecifier[]::new)는 Stream이 생성한 데이터로 해당 길이만큼 OrderSpecifier[] 배열을 생성하여 반환
		// `Stream.toArray(OrderSpecifier[]::new)`에서 `OrderSpecifier[]::new`를 지정해주는 이유
		//      - `OrderSpecifierUtil.generateOrderSpecifier()`의 리턴 타입이 OrderSpecifier<?>[]로 지정되기 때문에 컴파일 중 타입 에러 발생 방지하여 타입 안정성 제공
		//      - 불필요한 배열 생성과 복사 과정이 없어 성능 최적화 가능
	}

	// qClass에 프로퍼티 존재 여부 체크
	public static <T> boolean isPropertyExist(EntityPathBase<T> qClass, String propertyName) {
		Field[] fields = qClass.getClass().getDeclaredFields(); // 해당 QClass의 프로퍼티 리스트 획득

		// 프로퍼티 리스트에 해당 프로퍼티가 있으면 true, 없으면 false 리턴
		return Arrays.stream(fields).anyMatch(field -> field.getName().equals(propertyName));
	}
}
