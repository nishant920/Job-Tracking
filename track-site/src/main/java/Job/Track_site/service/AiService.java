package Job.Track_site.service;

import Job.Track_site.dto.InterviewQuestionDto;
import Job.Track_site.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class AiService {

    RestTemplate restTemplate;
    ObjectMapper objectMapper;
    public AiService(RestTemplate restTemplate, ObjectMapper objectMapper){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private final Map<String, List<String>> questionCache = new ConcurrentHashMap<>();

    @Value("${gemini.api-key}")
    private String geminiApiKey;

    /*Inside your getQuestions method, we need to build the prompt string that we will send to Gemini.
    We will customize it depending on whether the user provided a company name or years of experience.*/

    //-----------------------------------------caching check------------------------------------------------------------
    public List<String> getQuestions(InterviewQuestionDto questionDto){
        String cacheKey = questionDto.getProfile().toLowerCase();

        if(questionCache.containsKey(cacheKey)){
            log.info("Cache hit for profile: {}", cacheKey);
            return questionCache.get(cacheKey);
        }

        log.info("Manual Cache MISS for profile: {}. Calling Gemini API...", cacheKey);
//---------------------------------------------PROMPT GENERATION--------------------------------------------------------
        String profile = questionDto.getProfile();
        //start with base prompt
        StringBuilder prompt = new StringBuilder();
        prompt.append(String.format("Generate the top 10 most common interview questions for a %s role", profile));

        //add company if present
        if(questionDto.getCompanyName() != null && !questionDto.getCompanyName().isBlank()){
            prompt.append(String.format("Tailer the Question to specific %s", questionDto.getCompanyName()));
        }

        //add experince if present
        if(questionDto.getExperience() != null){
            prompt.append(String.format("Target candidate experience level: %d years.", questionDto.getExperience()));
        }

        String finalPrompt = prompt.toString();

        log.info("final build prompt : {}", finalPrompt);
//------------------------------------CALLING API - SETTING UP HTTP REQUEST BODY-------------------------------------
        // 1. Wrap prompt inside parts -> text
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", finalPrompt);

        // 2. Wrap parts in contents
        Map<String, Object> contentPart = new HashMap<>();
        contentPart.put("parts", Collections.singletonList(textPart));

        // 3. Create the top-level request map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", contentPart);

        // 4. Force Gemini to output a strict JSON array of strings
        Map<String, Object> generationConfig  = new HashMap<>();
        generationConfig.put("responseMimeType", "application/json");

        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("type", "ARRAY");

        Map<String, Object> itemSchema = new HashMap<>();
        itemSchema.put("type", "STRING");
        responseSchema.put("items", itemSchema);
        generationConfig.put("responseSchema", responseSchema);

        requestBody.put("generationConfig", generationConfig);
//------------------------------------------------HANDLING RESPONSE-----------------------------------------------------
        try{ //Configure HTTP headers to tell the API we are sending JSON data
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            //wrap request body(map) and headers into an HttpEntity

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key=" + geminiApiKey;

            //send post request to gemini
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            //Extract the generated text from Gemini's nested response body
            Map<String, Object> responseBody = response.getBody();
            if(responseBody!=null){
                //candidates is the container holding Gemini's actual answers

                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content"); //get > 0 array > to > map

                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts"); // > arraylist of map

                //now the actual response text
                /*When to Use (String) Cast > Use the explicit cast when you are certain the object is a String and a null value is acceptable or expected
                * When to Use .toString() > Use the method call when you want to convert any object into text and you know the object is never null*/
                String rawText = (String) parts.get(0).get("text");
                List<String> questions = objectMapper.readValue(rawText, new TypeReference<List<String>>() {});

                if(questions!=null && !questions.isEmpty()){
                    questionCache.put(cacheKey, questions);
                }

                return questions;

            }
            throw new RuntimeException("Empty response body from Gemini API");
        }catch (Exception e){
            log.warn("\"Gemini API call failed for profile {}. Falling back to mock data. Error: {}", profile, e.getMessage());

            return getMockQuestions(profile);
        }

    }

    public List<String> getMockQuestions(String profile){

        return new ArrayList<>(Arrays.asList(
                "Tell me about yourself and your background.",
                "What are your greatest strengths and weaknesses?",
                "Why are you interested in this position and our company?",
                "Describe a time you faced a difficult situation or obstacle at work/school and how you overcame it.",
                "How do you handle stress and pressure when deadlines are tight or things don't go as planned?",
                "Describe a time you had to work with someone whose working style or personality was very different from yours.",
                "Where do you see yourself professionally in five years?",
                "Tell me about a time you made a mistake. How did you handle it and what did you learn?",
                "How do you prioritize your time when managing multiple tasks or projects?",
                "Why should we hire you for this role?"
        ));
    }


}

/*
RequestBody
Google Gemini API expects us to send this exact JSON payload in the request body:

json


{
  "contents": [
    {
      "parts": [
        {
          "text": "Generate top 10 questions..."
        }
      ]
    }
  ],
  "generationConfig": {
    "responseMimeType": "application/json",
    "responseSchema": {
      "type": "ARRAY",
      "items": {
        "type": "STRING"
      }
    }
  }

  --------------------------------------------------------

  Response Body

  In the Gemini API response, candidates is a JSON array containing the different variations of responses generated by the model.
  Even though Gemini usually returns only one reply by default, the API design leaves room for the model to generate multiple alternative responses (known as "candidates") if you configure it to do so.

  The Anatomy of a Response
  {
  "candidates": [
    {
      "content": {
        "parts": [
          {
            "text": "Hello! I am Gemini. How can I help you today?"
          }
        ],
        "role": "model"
      },
      "finishReason": "STOP",
      "index": 0,
      "safetyRatings": [
        {
          "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
          "probability": "NEGLIGIBLE"
        }
      ]
    }
  ],
  "usageMetadata": {
    "promptTokenCount": 11,
    "candidatesTokenCount": 12,
    "totalTokenCount": 23
  }
}
Response Body (The whole JSON object)
 └── candidates (List of all generated response options)
      └── [0] (The first—and usually only—response option)
           └── content (The structural wrapper for the message)
                └── parts (List of pieces making up the message)
                     └── [0] (The first piece of data)
                          └── text ──> "This is the final text string!"

--------------------------------------------------------------------------
why try catch instead of global exception handling

-> The core difference is that local exception handling is for recovery and specific context,
 while a global exception handler is a safety net for fallback actions and application stability
-> As a general rule of thumb, you should only catch an exception locally if you can actually fix it or add meaningful context to it.
 Otherwise, you should let it bubble up to the global handler.

 Why final? (Reference Immutability & Thread Safety)
Prevents Reassignment: The final keyword guarantees that the variable reference can never be reassigned to null or replaced with another map object after initialization.
. Why private? (Encapsulation & Security )
Encapsulation (OOP Principle): The cache belongs only to AiService. Outside classes (like UserController or JobService) have no business touching or viewing the internal cache memory.
}*/
