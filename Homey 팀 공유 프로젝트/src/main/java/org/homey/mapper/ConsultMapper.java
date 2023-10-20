<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.homey.mapper.ConsultMapper">
<!-- 이 안에 실제 sql구문 추가 추가 -->	
<!-- id는 인터페이스의 메서드 명과 동일해야함 -->
<!-- resultType은 인터페이스의 이걸로, 알아서 담아줌, 여러개 나오면 리스트, 아니면 1개 나옴-->

  <!-- 쿼리 보관 -->
  <sql id="criteria">
  	 <trim prefix="(" suffix=") AND " prefixOverrides="OR">
	 	<foreach item="type" collection="typeArr">
	 	  <trim prefix="OR">
	 		<choose>
				<when test="type == 'C'.toString()"> <!-- int라서 문자열 패턴 적용안하고 비교 -->
				    consultNo = #{keyword}
				</when>
	 			<when test="type == 'B'.toString()">
	 				buildingType LIKE '%'||#{keyword}||'%'
	 			</when>
	 		</choose>
	 	  </trim>
	 	</foreach>
	 </trim>
  </sql>
  
  <select id="totalCount" resultType="int">
  	<![CDATA[ 
	  	SELECT 	COUNT(*) 
	  	FROM 	consult
	    WHERE	]]>
	     		<!-- 보관해 둔 쿼리 사용 -->
	    		<include refid="criteria"></include>
	<![CDATA[ 	consultNo > 0	]]>
  </select>
  
  <update id="update">
	UPDATE 	consult
	SET	
      happyCall = #{happyCall}	
      status = #{status}
	WHERE	consultNo = #{consultNo}  
  </update>
  
  <delete id="delete">
	DELETE consult WHERE consultNo = #{consultNo}
  </delete>

  
	<insert id="insert">
	  INSERT INTO consult(consultNo, mid, mphone, postcode, address, detailAddress, buildingType, scheduledDate, budget, pyeongsu, happyCall, consultDate, status)
	  VALUES(consult_seq.NEXTVAL, #{mid}, #{mphone}, #{postcode}, #{address}, #{detailAddress}, #{buildingType}, #{scheduledDate}, #{budget}, #{pyeongsu}, #{happyCall}, SYSDATE, '해피콜 예정')
	</insert>

	<select id="selectAllPaging" resultType="org.homey.domain.ConsultVO">
	  <![CDATA[
	  SELECT *
	  FROM (
	    SELECT
	      c.*,
	      ROW_NUMBER() OVER (ORDER BY consultNo DESC) AS rn
	    FROM consult c
	  ]]> 
	 <if test="keyword != null and keyword != ''">
	    <choose>
	      <when test="type == 'C'.toString()">
	        WHERE consultNo = #{keyword}
	      </when>
	      <when test="type == 'B'.toString()">
	        WHERE buildingType LIKE '%'||#{keyword}||'%'
	      </when>
	    </choose>
	  </if> 
	  <![CDATA[
	  )
	  WHERE rn <= #{amount} * #{pageNum}
	    AND rn > #{amount} * (#{pageNum} - 1)
	  ]]> 
	</select>
  
    <select id="selectAllPagingMe" 
  	      resultType="org.homey.domain.ConsultVO">
  	<![CDATA[ 
	    SELECT * FROM consult
	    WHERE mid = #{mid} AND consultNo > 0
	    ORDER BY consultNo DESC
    ]]>
  </select>
  
  <select id="select" 
  	      resultType="org.homey.domain.ConsultVO">
    SELECT * FROM consult WHERE consultNo = #{consultNo}
  </select>
  
</mapper>



